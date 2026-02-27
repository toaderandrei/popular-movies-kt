# App Architecture Documentation

## Overview

The TMDb App follows **Clean Architecture** with **MVVM pattern** and is built entirely with **Jetpack Compose**. All features use Compose screens, and the XML-to-Compose migration is complete (no Fragments, ViewBinding, or RecyclerView remain).

---

## Navigation Structure

### Visual Layout

**Top-level screens** (Movies, TV Shows, Favorites, Settings):
```
+-----------------------------------------+
|  Movies            search  account      | <- Top App Bar (animated)
+-----------------------------------------+
|                                         |
|         Screen Content Area             |
|         (Movies/TV/Favorites/Settings)  |
|                                         |
+-----------------------------------------+
|  Movies  TV Shows Favorites Settings    | <- Bottom Navigation
+-----------------------------------------+
```

**Detail screens** (Movie Details, TV Show Details, Categories, Search, Account):
```
+-----------------------------------------+
|  <- Movie Title          ♥              | <- MediumTopAppBar (collapsible)
|     Movie Title (expanded)              |
+-----------------------------------------+
|                                         |
|         Detail Content Area             |
|         (scrollable, no bottom nav)     |
|                                         |
+-----------------------------------------+
```

- Top App Bar (search/account) and Bottom Navigation are **hidden with animation** on non-top-level screens
- Detail screens use `MediumTopAppBar` with `exitUntilCollapsedScrollBehavior` for a smooth collapsing title effect
- Visibility is controlled by `MainContentState.shouldShowTopAppBar` (matches current route to `MainScreenDestination`)

### Navigation Graph

```
Root NavHost
+-- Authentication Flow (Graph.AUTHENTICATION)
|   +-- welcome (start destination) -- Welcome Screen
|   |   +-- "Login" -> login
|   |   +-- "Continue as Guest" -> Graph.MAIN
|   +-- login -- Login Screen
|       +-- success -> Graph.MAIN
|
+-- Main Flow (Graph.MAIN)
    +-- Inner NavHost (Bottom Nav + Top Bar)
        +-- movies (Bottom Nav)
        +-- tvshow (Bottom Nav)
        +-- favorites (Bottom Nav)
        +-- settings (Bottom Nav)
        +-- search (Top Bar Action)
        +-- account (Top Bar Action)
```

**Auth flow decision** (in `MainActivityViewModel`):
- `canSkipAuthentication()` returns `true` if user has a session OR guest mode is enabled -> `Graph.MAIN`
- Otherwise -> `Graph.AUTHENTICATION` (Welcome Screen)

### Navigation Destinations

**File:** `core/ui/src/main/kotlin/com/ant/ui/navigation/MainScreenDestination.kt`

| Destination | Route | Top Bar | Bottom Nav | Toolbar Style |
|---|---|---|---|---|
| Movies | `movies` | Main (search/account) | Shown | — |
| TV Shows | `tvshow` | Main (search/account) | Shown | — |
| Favorites | `favorites` | Main (search/account) | Shown | — |
| Settings | `settings` | Main (search/account) | Shown | — |
| Search | `search` | Hidden | Hidden | Own |
| Account | `account` | Hidden | Hidden | Own |
| Welcome | `welcome` | Hidden | Hidden | — |
| Login | `login` | Hidden | Hidden | — |
| Movie Details | `movies/details/{movieId}` | Hidden | Hidden | MediumTopAppBar (collapsible) |
| Movie Category | `movies/category/{categoryType}` | Hidden | Hidden | Own |
| TV Show Details | `tvshow/details/{tvShowId}` | Hidden | Hidden | MediumTopAppBar (collapsible) |
| TV Show Category | `tvshow/category/{categoryType}` | Hidden | Hidden | Own |

---

## Module Structure

```
app/                           # Main entry point, navigation, themes
build-logic/convention/        # Gradle convention plugins

features/
+-- movies/                    # Movie browsing and details
+-- tvshow/                    # TV series browsing and details
+-- favorites/                 # Saved favorites management
+-- search/                    # Search functionality
+-- login/                     # Authentication (login + welcome screen)

core/
+-- domain/                    # Use cases (business logic)
+-- data/                      # Repository implementations
+-- models/                    # Data models, entities, Room annotations
+-- database/                  # Room database
+-- network/                   # Network layer, data sources
+-- tmdbApi/                   # TMDb API client (uwetrottmann/tmdb-java)
+-- datastore/                 # DataStore preferences (session, guest mode)
+-- common/                    # Shared utilities, dispatchers, logger
+-- ui/                        # Shared UI (navigation destinations, Coil setup)
+-- resources/                 # Shared strings, drawables
+-- analytics/                 # Firebase Analytics/Crashlytics
```

### Module Dependencies

```
features/* --> core:domain, core:models, core:ui, core:resources
core:domain --> core:data, core:models
core:data --> core:models, core:network, core:database, core:tmdbApi
core:datastore --> core:common, core:models
app --> features/*, core/*
```

---

## Feature Architecture Pattern

### Standard Feature Structure

```
features/[feature-name]/
+-- [Feature]UiState.kt              # UI state data class
+-- [Feature]ViewModel.kt            # ViewModel with StateFlow
+-- ui/
|   +-- [Feature]Route.kt            # Entry point (ViewModel injection)
|   +-- [Feature]Screen.kt           # Pure UI composable
|   +-- components/                   # Feature-specific components
+-- details/
|   +-- [Feature]DetailsUiState.kt   # Details state
|   +-- [Feature]DetailsViewModel.kt # Details logic (load, fav toggle)
|   +-- [Feature]DetailsScreen.kt    # Details Route + Screen
+-- category/
|   +-- [Feature]CategoryUiState.kt  # Category list state
|   +-- [Feature]CategoryViewModel.kt# Category list logic
|   +-- ui/
|       +-- [Feature]CategoryRoute.kt
|       +-- [Feature]CategoryScreen.kt
+-- navigation/
    +-- [Feature]Navigation.kt        # Navigation setup & extensions
```

### Login Feature (includes Welcome Screen)

```
features/login/
+-- LoginViewModel.kt
+-- Credentials.kt
+-- state/LoginState.kt
+-- ui/
|   +-- LoginRoute.kt
|   +-- LoginScreen.kt
+-- welcome/
|   +-- WelcomeUiState.kt
|   +-- WelcomeViewModel.kt
|   +-- ui/
|   |   +-- WelcomeRoute.kt
|   |   +-- WelcomeScreen.kt
|   +-- navigation/
|       +-- WelcomeNavigation.kt
+-- navigation/
    +-- LoginNavigation.kt
```

---

## Data Flow

```
User Interaction
    |
Route (collects state via collectAsStateWithLifecycle)
    |
ViewModel (business logic, StateFlow)
    |
Use Case (domain layer, returns Flow<Result<T>>)
    |
Repository (data layer)
    |
Network (TMDb API) / Database (Room) / DataStore
    |
Flow<Result<T>> -- Result wrapper (Loading/Success/Error)
    |
ViewModel (updates StateFlow via .update{})
    |
Route (observes StateFlow)
    |
Screen (renders pure UI)
```

### Result Wrapper

```kotlin
sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error<T>(val throwable: Throwable) : Result<T>
    data object Loading : Result<Nothing>
}
```

### Use Case Pattern (Composition)

Use cases use the `resultFlow` top-level function instead of abstract class inheritance:

```kotlin
// core/domain/.../UseCase.kt
fun <R> resultFlow(
    dispatcher: CoroutineDispatcher,
    block: suspend () -> R,
): Flow<Result<R>>  // emits Loading -> Success or Error, on specified dispatcher
```

Each use case is a plain class with an `operator fun invoke(...)` returning `Flow<Result<T>>`.

### Repository Pattern

Repositories are plain `@Singleton` classes with `suspend fun performRequest()`. No base interface. Injected by concrete type via Hilt.

### Session & Guest Mode

**SessionManager** (`core/models`) defines the interface; **SessionManagerImpl** (`core/datastore`) persists via DataStore:

| Method | Purpose |
|---|---|
| `saveSessionId()` / `getSessionId()` | TMDb session persistence |
| `saveUsername()` / `getUsername()` | Username persistence |
| `setGuestMode()` / `isGuestMode()` | Guest mode flag |
| `canSkipAuthentication()` | Combined check: session OR guest mode |
| `clearSessionAndSignOut()` | Clears all preferences (session + guest) |

---

## Technology Stack

| Layer | Technologies |
|---|---|
| UI | Jetpack Compose, Material 3, Coil, Navigation Compose |
| State | StateFlow, Kotlin Coroutines, Flow |
| Architecture | MVVM, Clean Architecture, Hilt DI |
| Network | Retrofit + OkHttp, Kotlin Serialization |
| Persistence | Room, DataStore |
| Analytics | Firebase Analytics, Crashlytics |
| Testing | JUnit, MockK |

---

## Known Issues & Technical Debt

Review conducted 2026-02-21 using specialized agents. Issues prioritized by severity.

### Architecture

| Severity | Issue | Location |
|---|---|---|
| CRITICAL | `core:domain` depends on `core:data` (inverted dependency). Use cases import concrete repositories instead of interfaces. | `core/domain/build.gradle.kts` |
| HIGH | `core:models` has Room + Firebase dependencies (should be pure Kotlin) | `core/models/build.gradle.kts` |
| MEDIUM | Feature-to-feature dependencies: favorites/search depend on movies/tvshow for shared UI components (MovieCard, TvShowCard) | `features/favorites/`, `features/search/` |
| MEDIUM | `core:network` depends on `core:database` | `core/network/build.gradle.kts` |

### Concurrency

| Severity | Issue | Location | Status |
|---|---|---|---|
| ~~CRITICAL~~ | ~~Leaked `CoroutineScope(Dispatchers.IO)` in SessionManagerImpl~~ | `SessionManagerImpl.kt` | **FIXED** -- uses `@ApplicationScope` |
| CRITICAL | New `CoroutineScope` per call in AnalyticsHelperImpl (equivalent to GlobalScope) | `core/analytics/.../AnalyticsHelperImpl.kt:21` | Open |
| ~~CRITICAL~~ | ~~`runCatching` swallows `CancellationException` in `withRetry`~~ | `RetrofitExtensions.kt` | **FIXED** |
| HIGH | `CancellationException` swallowed in `SaveMovieDetailsUseCase` / `SaveTvSeriesDetailsUseCase` inner catch | `Save*UseCase.kt` | Open |
| HIGH | Sequential flow collection in FavoritesViewModel (movies blocks TV shows) | `FavoritesViewModel.kt` | Open |
| HIGH | Race condition: 4 parallel coroutines set `isLoading = false` independently | `MoviesViewModel.kt` | Open |
| MEDIUM | No job cancellation on refresh in MoviesViewModel / TvShowViewModel | `MoviesViewModel.kt`, `TvShowViewModel.kt` | Open |
| ~~MEDIUM~~ | ~~No application-scoped CoroutineScope via Hilt DI~~ | `CoroutinesModule.kt` | **FIXED** -- `@ApplicationScope` added |

### Compose UI

| Severity | Issue | Location | Status |
|---|---|---|---|
| ~~HIGH~~ | ~~Navigation side effect in LoginScreen instead of LoginRoute~~ | `LoginRoute.kt` | **FIXED** |
| HIGH | Cross-feature UI dependency: MovieCard/TvShowCard used from favorites and search (should be in core:ui) | `features/favorites/`, `features/search/` | Open |
| DONE | `@Preview` added to LoginScreen, WelcomeScreen, MovieDetailsScreen, TvShowDetailsScreen | Various | **FIXED** |
| MEDIUM | ~30 hardcoded strings remain (down from 50+) | Movies, TvShow, Details screens | Partial |
| MEDIUM | Hardcoded colors breaking dark mode | RatingBadge, WelcomeScreen, PopularMoviesBackground | Open |
| MEDIUM | Duplicated composables: RatingBadge, LoadingState, ErrorState, EmptyState across 6 files | `features/movies/`, `features/tvshow/` | Open |

### ViewModel

| Severity | Issue | Location | Status |
|---|---|---|---|
| CRITICAL | One-off events (Success, Canceled) as persistent StateFlow -- double navigation on rotation | LoginViewModel, WelcomeViewModel, AccountViewModel | Open |
| ~~MEDIUM~~ | ~~Missing `.asStateFlow()` in MainActivityViewModel~~ | `MainActivityViewModel.kt` | **FIXED** |
| MEDIUM | Missing `.asStateFlow()` in LoginViewModel | `LoginViewModel.kt:33` | Open |
| LOW | Direct `.value =` instead of `.update{}` (inconsistent with codebase pattern) | LoginViewModel | Open |

### Gradle / Build

| Severity | Issue | Location |
|---|---|---|
| CRITICAL | Redundant Hilt dependencies declared both by convention plugin AND manually in 4+ modules | core:tmdbApi, core:domain, core:analytics, app |
| MAJOR | ~20+ unused entries in version catalog (legacy View-based libraries) | `gradle/libs.versions.toml` |
| MAJOR | 4 duplicate lifecycle version entries, 2 duplicate navigation versions | `gradle/libs.versions.toml` |
| MAJOR | Feature convention plugin missing common dependencies (core:ui, core:domain, Compose) -- repeated in all 5 features | `AndroidFeatureConventionPlugin.kt` |
| MODERATE | Debug `println` in HiltConventionPlugin and AndroidBuildConfigPlugin | `build-logic/convention/` |
| MODERATE | Dual serialization: Gson + Kotlin Serialization both in use | `core/models/`, `core/database/` |
| MINOR | `settings.gradle` is Groovy, rest of project uses Kotlin DSL | Root |

### Testing

| Severity | Issue | Location |
|---|---|---|
| CRITICAL | 0% test coverage -- zero real tests exist | Entire project |
| HIGH | Missing `kotlinx-coroutines-test` dependency (not in catalog) | `gradle/libs.versions.toml` |
| HIGH | `mockK` declared in catalog but not used in any module | `gradle/libs.versions.toml` |
| MEDIUM | 12 stale NiA test files + 15 placeholder tests to delete | Multiple modules |
| MEDIUM | No shared test infrastructure (MainDispatcherRule, fakes) | Missing `core:testing` |

### Migration Cleanup

| Status | Item |
|---|---|
| DONE | XML layouts, ViewBinding, DataBinding, Epoxy, RecyclerView, Fragments, Navigation XML |
| DONE | Legacy Fragment/ViewModel files removed from app module |
| DONE | UseCase inheritance replaced with `resultFlow` composition |
| DONE | Repository `open` modifiers and `Repository<P,R>` interface removed |
| DONE | `Repository.kt` interface file deleted |
| DONE | `FavoriteType.MOVIE` bug fixed in TV series use cases |
| DONE | `CancellationException` swallowing fixed in `resultFlow` and `withRetry` |
| DONE | Artificial 100ms delay removed from use cases |
| DONE | `SessionManagerImpl` uses `@ApplicationScope` instead of leaked `CoroutineScope(Dispatchers.IO)` |
| DONE | `@ApplicationScope` qualifier added to `CoroutinesModule` with `SupervisorJob()` |
| DONE | `MainActivityViewModel` uses `.asStateFlow()` |
| DONE | `MainActivityViewModel` redundant nested coroutine simplified |
| DONE | Navigation side effect moved from LoginScreen to LoginRoute |
| DONE | FavoritesScreen fully qualified imports fixed |
| DONE | Previews added for Login, Welcome, category screens |
| DONE | Some hardcoded strings migrated to `stringResource()` |
| DONE | Detail screens implemented with Route/Screen separation (Movies, TV Shows) |
| DONE | Category list screens implemented (Movies, TV Shows) |
| DONE | Navigation wiring for all detail and category screens |
| DONE | Top bar and bottom nav hidden on detail/nested screens (animated) |
| DONE | Collapsible MediumTopAppBar on Movie/TV Show detail screens |
| DONE | Duplicate LazyRow key crash fixed (itemsIndexed fallback) |
| REMAINING | Delete dead `MainActivity.kt` (commented out in manifest) |
| REMAINING | Delete stale NowInAndroid test files (12 files across 6 modules) |
| REMAINING | Delete unused drawables (side_nav_bar.xml, ic_camera, ic_gallery, ic_slideshow, fading_snackbar_background) |
| REMAINING | Delete unused `feature_foryou_ic_icon_placeholder.xml` across all features |
| REMAINING | Clean up unused version catalog entries |
| REMAINING | Rename `core/tmdbApi/old/` package |

---

## Best Practices

### When Adding New Features

1. Follow the `features:movies` structure as the reference
2. Create `UiState` data class first
3. Implement pure Screen composable (stateless, accepts state + callbacks)
4. Create Route composable for ViewModel injection
5. Set up navigation with `NavGraphBuilder` extension functions
6. Use `StateFlow` with `.asStateFlow()` and `.update{}` for state
7. Use `SharedFlow(replay=0)` for one-off events (navigation, toasts)
8. Handle Loading/Success/Error states
9. Add `@Preview` functions for all public composables
10. Use `stringResource()` instead of hardcoded strings

### Composable Design

- Accept `modifier: Modifier = Modifier` as first optional parameter
- Apply modifier to root element
- Keep composables pure -- no side effects in Screen composables
- Side effects (LaunchedEffect for navigation) belong in Route composables
- Use `MaterialTheme` colors and typography, avoid hardcoded values
- Use method references (`viewModel::refresh`) for stable lambdas

### ViewModel Patterns

- Private `MutableStateFlow`, exposed via `.asStateFlow()`
- Use `.update{}` for thread-safe state modifications
- Delegate to UseCases/Repositories, not direct data access
- Use `viewModelScope` for all coroutine launches
- Cancel previous jobs when re-launching (debounce pattern)

---

## Key Files Reference

| Purpose | File |
|---|---|
| Root navigation | `app/.../MainApp.kt` |
| Auth state | `app/.../viewmodel/MainActivityViewModel.kt` |
| Nav destinations | `core/ui/.../MainScreenDestination.kt` |
| Session management | `core/models/.../SessionManager.kt` |
| Session impl | `core/datastore/.../SessionManagerImpl.kt` |
| `resultFlow` helper | `core/domain/.../UseCase.kt` |
| Result wrapper | `core/models/.../Result.kt` |
| Theme | `app/.../themes/Theme.kt` |
| Convention plugins | `build-logic/convention/src/main/kotlin/` |
| Version catalog | `gradle/libs.versions.toml` |
