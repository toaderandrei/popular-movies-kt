---
name: kmp-architecture
description: Expert guidance on setting up and maintaining a modern Kotlin Multiplatform application architecture using Clean Architecture and Koin. Use this when asked about project structure, module setup, dependency injection, or shared/platform-specific code boundaries.
---

# Kotlin Multiplatform Modern Architecture & Modularization

## Instructions

When designing or refactoring a KMP application, adhere to **Clean Architecture** principles adapted for multiplatform. The goal is to maximize shared code in `commonMain` while cleanly isolating platform-specific implementations behind `expect`/`actual` or interfaces.

### 1. High-Level Layers

Structure the application into three primary layers. Dependencies must strictly flow **inwards** to the core logic. All layers live in `commonMain` unless they require platform APIs.

*   **UI Layer (Presentation)**:
    *   **Responsibility**: Displaying data and handling user interactions.
    *   **Components**: Compose Multiplatform screens, ViewModels (shared in `commonMain`).
    *   **Platform Split**:
        *   Android: `ComponentActivity` hosts, Material 3 theming.
        *   iOS: `UIViewController` integration via `ComposeUIViewController`.
        *   Desktop/Web: Platform entry points wrapping shared Compose UI.
    *   **Dependencies**: Depends on the Domain Layer only. **Never** depends on Data Layer implementation details directly.

*   **Domain Layer (Business Logic) [Required for KMP]**:
    *   **Responsibility**: Encapsulating business rules, shared across all platforms.
    *   **Components**: Use Cases (e.g., `GetOrdersUseCase`), Domain Models (pure Kotlin data classes).
    *   **Pure Kotlin**: Must NOT contain any platform dependencies — no `android.*`, no `platform.UIKit.*`, no `java.*` imports.
    *   **Dependencies**: Depends on Repository Interfaces (defined here, implemented in Data Layer).
    *   **Location**: Always in `commonMain`.

*   **Data Layer**:
    *   **Responsibility**: Managing application data (fetching, caching, persistence).
    *   **Components**: Repository implementations, Data Sources (Ktor client for networking, SQLDelight/Room for local storage).
    *   **Platform Split**: Network engines and database drivers are provided per-platform via Koin modules or `expect`/`actual`.
    *   **Dependencies**: Depends only on external sources and libraries.

### 2. Dependency Injection with Koin

Use **Koin** for all dependency injection — it's Kotlin-native, works identically across all KMP targets, and requires no annotation processing or code generation.

*   **No annotations required**: Unlike Hilt, there are no `@HiltAndroidApp`, `@AndroidEntryPoint`, or `@HiltViewModel` equivalents. Koin uses pure Kotlin DSL — no kapt/KSP, no generated code, no platform-specific DI setup.
*   **Module DSL**: Define dependencies in `module { }` blocks using `single` (singleton), `factory` (new instance each time), and `viewModelOf` (ViewModel scoping).
*   **Interface binding**: Use `single<Interface> { Implementation(get()) }` — equivalent to Hilt's `@Binds` but without abstract classes.
*   **Scoping**: Use `scope<ScopeType> { }` for lifecycle-bound instances when needed (e.g., per-screen or per-flow scoping).

*   **Shared Modules** (`commonMain`):
    ```kotlin
    // di/CommonModules.kt
    val domainModule = module {
        factory { GetOrdersUseCase(get()) }
        factory { SyncOrdersUseCase(get(), get()) }
    }

    val dataModule = module {
        single<OrderRepository> { OrderRepositoryImpl(get(), get()) }
        single { OrderRemoteDataSource(get()) }
    }

    val viewModelModule = module {
        viewModelOf(::OrderListViewModel)
        viewModelOf(::OrderDetailViewModel)
    }

    fun commonModules() = listOf(domainModule, dataModule, viewModelModule)
    ```

*   **Platform Modules** (`androidMain` / `iosMain` / `desktopMain`):
    ```kotlin
    // androidMain/di/PlatformModules.kt
    val platformModule = module {
        single<HttpClientEngine> { Android.create() }
        single<DatabaseDriver> { AndroidSqliteDriver(AppDatabase.Schema, get(), "app.db") }
        single { androidContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }
    }

    // iosMain/di/PlatformModules.kt
    val platformModule = module {
        single<HttpClientEngine> { Darwin.create() }
        single<DatabaseDriver> { NativeSqliteDriver(AppDatabase.Schema, "app.db") }
    }
    ```

*   **Initialization**:
    ```kotlin
    // commonMain
    fun initKoin(platformModules: List<Module> = emptyList()) {
        startKoin {
            modules(commonModules() + platformModules)
        }
    }

    // Android: Application.onCreate()
    initKoin(listOf(platformModule))

    // iOS: via helper function called from Swift
    fun initKoinIos() = initKoin(listOf(platformModule))
    ```

*   **ViewModel Injection** (Koin 4.x with KMP ViewModel support):
    ```kotlin
    // In Compose Multiplatform
    @Composable
    fun OrderListScreen(
        viewModel: OrderListViewModel = koinViewModel()
    ) { ... }
    ```

### 3. Modularization Strategy

For production KMP apps, use a multi-module strategy. Each module is a KMP module with its own `commonMain`, `androidMain`, `iosMain`, etc.

```
:composeApp              → Platform entry points (Android Activity, iOS ComposeUIViewController, Desktop main)
:shared                  → Umbrella module re-exporting shared API (optional, for iOS framework export)

:core:model              → Shared domain models (Pure Kotlin, commonMain only)
:core:data               → Repository impls, Data Sources, Ktor client setup, SQLDelight/Room
:core:domain             → Use Cases, Repository Interfaces (Pure Kotlin, commonMain only)
:core:ui                 → Shared Compose Multiplatform components, Theme, Design System
:core:common             → Utility extensions, expect/actual helpers (logging, dispatchers, UUID)

:feature:[name]          → Standalone feature modules with their own UI + ViewModels
                           Depends on :core:domain and :core:ui
                           Contains commonMain + platform source sets as needed
```

**Module dependency rules:**
*   `:feature:*` → `:core:domain`, `:core:ui`, `:core:model`
*   `:core:data` → `:core:domain`, `:core:model`
*   `:core:domain` → `:core:model`
*   `:core:ui` → `:core:model`
*   `:core:model` → nothing (pure Kotlin)
*   `:core:domain` → nothing platform-specific (pure Kotlin)
*   `:composeApp` → `:feature:*`, `:core:data` (for Koin wiring)

### 4. expect/actual Strategy

Use `expect`/`actual` sparingly — prefer interfaces + Koin injection for most platform splits. Reserve `expect`/`actual` for:

*   **Platform primitives**: `UUID`, `Parcelable`, `Instant`/date-time if not using kotlinx-datetime.
*   **Platform engines**: Ktor `HttpClientEngine`, SQLDelight `SqlDriver`.
*   **Platform capabilities**: Logging, file system access, connectivity checks.

```kotlin
// commonMain
expect fun createPlatformLogger(tag: String): Logger

// androidMain
actual fun createPlatformLogger(tag: String): Logger = AndroidLogger(tag)

// iosMain
actual fun createPlatformLogger(tag: String): Logger = NSLogLogger(tag)
```

For anything with multiple dependencies or lifecycle concerns, prefer **interface + Koin**:

```kotlin
// commonMain
interface ConnectivityMonitor {
    val isConnected: StateFlow<Boolean>
}

// Provided via platform Koin module
// single<ConnectivityMonitor> { AndroidConnectivityMonitor(get()) }
```

### 5. Recommended KMP Stack

| Concern            | Library                        | Notes                                      |
|--------------------|--------------------------------|--------------------------------------------|
| UI                 | Compose Multiplatform          | Shared UI across Android, iOS, Desktop     |
| Navigation         | Voyager / Decompose            | KMP-native navigation                      |
| DI                 | Koin                           | No codegen, works everywhere               |
| Networking         | Ktor Client                    | Platform engines via Koin                   |
| Serialization      | kotlinx-serialization          | Multiplatform JSON/Protobuf                |
| Local DB           | SQLDelight / Room KMP          | SQLDelight more mature for KMP             |
| Async              | kotlinx-coroutines             | Structured concurrency, shared dispatchers |
| Date/Time          | kotlinx-datetime               | Replaces java.time across platforms        |
| Image Loading      | Coil 3                         | KMP support with Compose                   |
| Logging            | Kermit / Napier                | Multiplatform logging                      |
| Key-Value Storage  | multiplatform-settings         | SharedPrefs/NSUserDefaults abstraction     |
| ViewModel          | KMP ViewModel (Jetbrains)      | Lifecycle-aware, works with Koin 4.x       |

### 6. Gradle Configuration

Use **Version Catalogs** (`libs.versions.toml`) and **Convention Plugins** for consistent build configuration:

```kotlin
// build-logic/convention/src/main/kotlin/KmpLibraryConventionPlugin.kt
class KmpLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("org.jetbrains.kotlin.multiplatform")
        }
        extensions.configure<KotlinMultiplatformExtension> {
            androidTarget()
            iosX64()
            iosArm64()
            iosSimulatorArm64()

            sourceSets {
                commonMain.dependencies {
                    implementation(libs.findLibrary("kotlinx-coroutines-core").get())
                    implementation(libs.findLibrary("koin-core").get())
                }
                commonTest.dependencies {
                    implementation(kotlin("test"))
                    implementation(libs.findLibrary("koin-test").get())
                }
            }
        }
    }
}
```

Apply via:
```kotlin
// feature/orders/build.gradle.kts
plugins {
    id("app.kmp.library")
    id("app.kmp.compose")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.domain)
            implementation(projects.core.ui)
        }
    }
}
```

### 7. Checklist for Implementation

- [ ] Domain layer has **zero** platform dependencies — only pure Kotlin in `commonMain`
- [ ] All repository interfaces are defined in `:core:domain`, implementations in `:core:data`
- [ ] Platform-specific code is isolated in `expect`/`actual` or platform Koin modules
- [ ] ViewModels live in `commonMain`, use `StateFlow` for state, are injected via `koinViewModel()`
- [ ] Ktor `HttpClientEngine` is provided per-platform via Koin, never hardcoded
- [ ] SQLDelight/Room `Driver` is provided per-platform via Koin
- [ ] No `java.*` imports in `commonMain` (use `kotlinx-datetime`, `kotlinx-io`, etc.)
- [ ] Convention plugins handle shared Gradle config — no copy-paste across `build.gradle.kts` files
- [ ] Feature modules depend only on `:core:*`, never on other `:feature:*` modules
- [ ] Coroutine dispatchers are injectable (testable) — use `Dispatchers.Default`/`IO` via a `DispatcherProvider` interface
- [ ] iOS framework export is configured in `:shared` or `:composeApp` with proper API visibility
