---
name: kmp-testing
description: Comprehensive testing strategy for Kotlin Multiplatform projects involving Unit, Integration, Koin, and Screenshot tests across commonMain and platform targets.
---

# KMP Testing Strategies

This skill provides expert guidance on testing Kotlin Multiplatform applications. It covers **Unit Tests** (shared logic), **Integration Tests** (Koin-wired components, database, networking), and **Screenshot Testing** (Compose Multiplatform UI).

## Testing Pyramid

1.  **Unit Tests** (`commonTest`): Fast, isolate pure logic — Use Cases, Repositories, mappers, ViewModels. Run on all targets.
2.  **Integration Tests** (`commonTest` or platform test sets): Test interactions with Koin DI, database (SQLDelight), networking (Ktor MockEngine).
3.  **UI/Screenshot Tests** (`androidUnitTest` / `desktopTest`): Verify UI correctness of Compose Multiplatform screens.

## Key Principle: Test in `commonTest` First

Maximize tests in `commonTest` — they run on every target and catch platform assumptions early. Only drop to `androidUnitTest`, `iosTest`, or `desktopTest` when testing platform-specific behavior (e.g., Android Context, iOS NSUserDefaults).

## Dependencies (`libs.versions.toml`)

```toml
[versions]
kotlinxCoroutines = "1.9.0"
koin = "4.0.4"
ktor = "3.1.1"
roborazzi = "1.40.0"

[libraries]
# Shared (commonTest)
kotlin-test = { module = "org.jetbrains.kotlin:kotlin-test" }
kotlinx-coroutines-test = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-test", version.ref = "kotlinxCoroutines" }
koin-test = { group = "io.insert-koin", name = "koin-test", version.ref = "koin" }
ktor-client-mock = { group = "io.ktor", name = "ktor-client-mock", version.ref = "ktor" }
turbine = { group = "app.cash.turbine", name = "turbine", version = "1.2.0" }

# Android-specific tests
androidx-test-ext-junit = { group = "androidx.test.ext", name = "junit", version = "1.2.1" }
compose-ui-test = { group = "org.jetbrains.compose.ui", name = "ui-test-junit4" }
roborazzi-core = { group = "io.github.takahirom.roborazzi", name = "roborazzi", version.ref = "roborazzi" }
roborazzi-compose = { group = "io.github.takahirom.roborazzi", name = "roborazzi-compose", version.ref = "roborazzi" }

[plugins]
roborazzi = { id = "io.github.takahirom.roborazzi", version.ref = "roborazzi" }
```

### Gradle Setup (per module)

```kotlin
// feature/orders/build.gradle.kts
kotlin {
    sourceSets {
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.koin.test)
            implementation(libs.ktor.client.mock)
            implementation(libs.turbine)
        }
        // Android screenshot / instrumented tests
        val androidUnitTest by getting {
            dependencies {
                implementation(libs.compose.ui.test)
                implementation(libs.roborazzi.core)
                implementation(libs.roborazzi.compose)
            }
        }
    }
}
```

---

## 1. Unit Tests (`commonTest`)

### Testing Use Cases

```kotlin
// commonTest/domain/GetOrdersUseCaseTest.kt
class GetOrdersUseCaseTest {

    private val repository = FakeOrderRepository()
    private val useCase = GetOrdersUseCase(repository)

    @Test
    fun returnsOrdersSortedByDate() = runTest {
        repository.emit(listOf(
            Order(id = "1", date = LocalDate(2025, 1, 15)),
            Order(id = "2", date = LocalDate(2025, 2, 1)),
        ))

        val result = useCase().first()

        assertEquals("2", result.first().id)
    }
}
```

### Testing ViewModels with Turbine

```kotlin
// commonTest/ui/OrderListViewModelTest.kt
class OrderListViewModelTest {

    private val repository = FakeOrderRepository()
    private val viewModel = OrderListViewModel(GetOrdersUseCase(repository))

    @Test
    fun initialStateIsLoading() = runTest {
        viewModel.uiState.test {
            assertEquals(OrderListUiState.Loading, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun ordersLoadedSuccessfully() = runTest {
        val orders = listOf(Order(id = "1", title = "Burger"))
        repository.emit(orders)

        viewModel.uiState.test {
            skipItems(1) // skip Loading
            val state = awaitItem()
            assertIs<OrderListUiState.Success>(state)
            assertEquals(1, state.orders.size)
        }
    }
}
```

### Fake Implementations (in `commonTest`)

```kotlin
// commonTest/data/FakeOrderRepository.kt
class FakeOrderRepository : OrderRepository {

    private val ordersFlow = MutableSharedFlow<List<Order>>()

    suspend fun emit(orders: List<Order>) = ordersFlow.emit(orders)

    override fun getOrders(): Flow<List<Order>> = ordersFlow

    override suspend fun syncOrders(): Result<Unit> = Result.success(Unit)
}
```

### Testing Coroutine Dispatchers

Inject dispatchers so tests can control execution:

```kotlin
// commonMain
interface DispatcherProvider {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
}

// commonTest
class TestDispatcherProvider(
    testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : DispatcherProvider {
    override val main = testDispatcher
    override val io = testDispatcher
    override val default = testDispatcher
}
```

---

## 2. Integration Tests with Koin

No `HiltAndroidRule`, no annotations — just start/stop Koin in your test.

### Basic Koin Test

```kotlin
// commonTest/di/KoinModuleCheckTest.kt
class KoinModuleCheckTest : KoinTest {

    @Test
    fun verifyKoinConfiguration() {
        startKoin {
            modules(commonModules())
        }.checkModules()
        stopKoin()
    }
}
```

### Testing with Koin-Injected Dependencies

```kotlin
// commonTest/data/OrderRepositoryIntegrationTest.kt
class OrderRepositoryIntegrationTest : KoinTest {

    private val repository: OrderRepository by inject()

    @BeforeTest
    fun setUp() {
        startKoin {
            modules(
                module {
                    single<HttpClientEngine> { MockEngine { respondOk(fakeOrdersJson) } }
                    single { OrderRemoteDataSource(HttpClient(get())) }
                    single<OrderRepository> { OrderRepositoryImpl(get()) }
                }
            )
        }
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun fetchOrdersFromRemote() = runTest {
        val orders = repository.getOrders().first()
        assertEquals(2, orders.size)
    }
}
```

### Testing with Ktor MockEngine

```kotlin
// commonTest/data/OrderRemoteDataSourceTest.kt
class OrderRemoteDataSourceTest {

    @Test
    fun parsesOrdersCorrectly() = runTest {
        val mockEngine = MockEngine { request ->
            when (request.url.encodedPath) {
                "/api/orders" -> respondOk(
                    content = Json.encodeToString(listOf(OrderDto(id = "1", title = "Pizza"))),
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                )
                else -> respondError(HttpStatusCode.NotFound)
            }
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) { json() }
        }
        val dataSource = OrderRemoteDataSource(client)

        val orders = dataSource.fetchOrders()
        assertEquals("Pizza", orders.first().title)
    }
}
```

### Testing SQLDelight DAOs

```kotlin
// commonTest (or androidUnitTest if using Android driver)
class OrderDaoTest {

    private lateinit var database: AppDatabase

    @BeforeTest
    fun setUp() {
        // Use in-memory driver — JdbcSqliteDriver for JVM/Android tests
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        AppDatabase.Schema.create(driver)
        database = AppDatabase(driver)
    }

    @Test
    fun insertAndRetrieveOrder() {
        val dao = database.orderQueries

        dao.insert(id = "1", title = "Burger", status = "pending")
        val result = dao.getAll().executeAsList()

        assertEquals(1, result.size)
        assertEquals("Burger", result.first().title)
    }
}
```

---

## 3. Screenshot Testing (Compose Multiplatform)

### Approach: Roborazzi for Android Target

Roborazzi runs on the JVM via Robolectric — no emulator needed. For KMP, screenshot tests target the Android source set but test shared Composables.

#### Setup

```kotlin
// feature/orders/build.gradle.kts
plugins {
    alias(libs.plugins.roborazzi)
}
```

#### Writing a Screenshot Test

```kotlin
// androidUnitTest/ui/OrderListScreenScreenshotTest.kt
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = RobolectricDeviceQualifiers.Pixel5)
class OrderListScreenScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun captureOrderListLoading() {
        composeTestRule.setContent {
            AppTheme {
                OrderListScreen(
                    uiState = OrderListUiState.Loading
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun captureOrderListWithItems() {
        composeTestRule.setContent {
            AppTheme {
                OrderListScreen(
                    uiState = OrderListUiState.Success(
                        orders = listOf(
                            Order(id = "1", title = "Burger", status = "ready"),
                            Order(id = "2", title = "Pizza", status = "pending"),
                        )
                    )
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }
}
```

### Tip: Make Composables Screenshot-Friendly

Design screens to accept `uiState` as a parameter so screenshot tests can render any state without Koin/ViewModel wiring:

```kotlin
// commonMain - the screen Composable
@Composable
fun OrderListScreen(
    uiState: OrderListUiState,        // ← testable, no ViewModel needed
    onOrderClick: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) { ... }

// commonMain - the route/wired version
@Composable
fun OrderListRoute(
    viewModel: OrderListViewModel = koinViewModel(),
    onOrderClick: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    OrderListScreen(uiState = uiState, onOrderClick = onOrderClick)
}
```

### Desktop Screenshot Tests (Alternative)

For non-Android screenshot testing using Compose Desktop test APIs:

```kotlin
// desktopTest/ui/OrderCardDesktopTest.kt
class OrderCardDesktopTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun renderOrderCard() = runDesktopComposeUiTest {
        setContent {
            AppTheme {
                OrderCard(
                    order = Order(id = "1", title = "Burger", status = "ready")
                )
            }
        }
        onRoot().assertExists()
        // captureToImage() available for desktop screenshot comparison
    }
}
```

---

## 4. Test Organization

```
:feature:orders/
├── commonMain/          → Production code (shared)
├── commonTest/          → Unit tests: UseCases, ViewModels, Fakes, Koin checks
│   ├── domain/
│   ├── data/
│   ├── ui/              → ViewModel tests
│   └── di/              → Koin module verification
├── androidUnitTest/     → Screenshot tests (Roborazzi), Android-specific integration
├── desktopTest/         → Desktop Compose UI tests
└── iosTest/             → iOS-specific tests (rarely needed)
```

---

## 5. Running Tests

| Command                                | What it does                                      |
|----------------------------------------|---------------------------------------------------|
| `./gradlew allTests`                   | Run all tests across all targets                  |
| `./gradlew :feature:orders:commonTest` | Shared unit tests only (fast)                     |
| `./gradlew testDebugUnitTest`          | Android unit tests (includes Roborazzi)           |
| `./gradlew recordRoborazziDebug`       | Record screenshot baselines                       |
| `./gradlew verifyRoborazziDebug`       | Verify screenshots against baselines              |
| `./gradlew desktopTest`                | Desktop target tests                              |
| `./gradlew iosSimulatorArm64Test`      | iOS tests (requires macOS)                        |

---

## 6. Checklist

- [ ] Business logic tests live in `commonTest` — not in any platform test set
- [ ] Fakes (not mocks) are preferred — `FakeOrderRepository` over Mockk/Mockito
- [ ] Koin modules are verified with `checkModules()` in `commonTest`
- [ ] ViewModels are tested with Turbine for `StateFlow` assertions
- [ ] Coroutine dispatchers are injected via `DispatcherProvider` — never hardcoded `Dispatchers.IO` in ViewModels
- [ ] Ktor networking is tested with `MockEngine` — no real HTTP calls in tests
- [ ] Screenshot tests use stateless Composable signatures (`uiState` parameter, not ViewModel)
- [ ] Screenshot baselines are committed to version control
- [ ] `runTest` is used for all coroutine tests (not `runBlocking`)
- [ ] No Hilt/Dagger dependencies anywhere — Koin `startKoin`/`stopKoin` in `@BeforeTest`/`@AfterTest`
