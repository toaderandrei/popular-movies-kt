# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Before Writing Code
- Explain clearly what and why before proposing changes
- If it is a new topic, do a deep dive first (preferably use architect mode)
- Always document existing findings and current solution
- Never write code without user approval

## Writing Code
- All code should be testable
- Use SOLID principles as much as possible
- Use Android guidelines when writing code
- For Gradle always look at `build-logic/` folder for inspiration or for updating existing plugins
- Always follow existing naming and formatting conventions in files

## Testing Conventions
- Test names should always start with "Should"
- If tests are repeatable, try to use Parameterized tests
- If a test needs data that is reusable in other tests, try to use a `TestData` class (create one if it doesn't exist)
- Run single test: `./gradlew :module:test --tests "TestClass.testMethod"`

## Merge Requests
Use this format for MR descriptions:

### Problem
- Explain the problem in detail with reproduction steps

### Implementation
- Explain the solution in detail, why it was chosen, and how it fixes the problem

#### Code Changes
- Explain code changes at a high level: classes involved and changes made (not implementation details)

### Testing
- Explain how the solution was tested

### Demo
- If possible, add a video of the solution working

## Project Overview

Popular Movies is a Kotlin-based Android application that showcases popular movies and TV series using The Movie Database (TMDb) API. The app follows Clean Architecture principles with MVVM pattern and is organized into feature modules.

## Architecture

### Clean Architecture Layers

The project follows Clean Architecture with clear separation of concerns:

- **app**: Main application entry point, UI composition, and navigation
  - Uses `MainActivityCompose` with Jetpack Compose
  - All UI is Compose-based (XML/Fragment migration complete)

- **features**: Feature modules organized by business domain:
  - `features:movies` - Movie browsing, categories, and details
  - `features:tvshow` - TV series browsing, categories, and details
  - `features:favorites` - Saved favorites management
  - `features:search` - Search functionality
  - `features:login` - User authentication via TMDb (login + welcome + account)

  **Feature Module Structure** (see `features:movies` as reference):
  ```
  features/[feature-name]/
  ├── [Feature]UiState.kt          # UI state data class
  ├── [Feature]ViewModel.kt        # ViewModel with StateFlow
  ├── ui/
  │   ├── [Feature]Route.kt        # Entry point (ViewModel injection)
  │   ├── [Feature]Screen.kt       # Pure UI composable
  │   └── components/              # Feature-specific components
  ├── details/                     # Detail screens (if applicable)
  ├── category/                    # Category list screens (if applicable)
  └── navigation/
      └── [Feature]Navigation.kt   # Navigation setup & extensions
  ```

- **core**: Shared infrastructure and business logic
  - `core:domain` - Use cases (business logic orchestration)
  - `core:data` - Repository implementations
  - `core:database` - Room database for local persistence
  - `core:network` - Network layer configuration
  - `core:tmdbApi` - TMDb API client interfaces
  - `core:datastore` - DataStore for preferences/settings
  - `core:models` - Data models and DTOs
  - `core:common` - Shared utilities, dispatcher qualifiers, DI modules
  - `core:ui` - Shared UI components, navigation destinations
  - `core:resources` - Shared resources (strings, drawables, etc.)
  - `core:analytics` - Analytics/Firebase integration

- **build-logic**: Gradle convention plugins for consistent build configuration
  - Uses composite builds pattern (included build)
  - Custom convention plugins define common configurations across modules

### Data Flow Pattern

1. **UI Layer** (Feature modules) → ViewModels observe UI events
2. **Domain Layer** (Use Cases) → Orchestrates business logic, returns `Flow<Result<T>>`
   - `resultFlow(dispatcher)` wraps execution in Loading/Success/Error states with `flowOn`
   - Properly rethrows `CancellationException` for structured concurrency
3. **Data Layer** (Repositories) → Handles data sources (remote/local)
   - Concrete `@Singleton` classes with `suspend fun performRequest()`
   - Coordinates between network (TMDb API), database (Room), and DataStore

### Key Patterns

- **Result Wrapper**: All use cases return `Flow<Result<T>>` where Result is Loading, Success, or Error
- **Pagination**: List endpoints return `PaginatedResult<T>` (items + page + totalPages). Category screens implement infinite scroll via `LazyGridState` + `derivedStateOf`. Home/search screens extract `.items` only.
- **Repository Pattern**: Single responsibility repositories (e.g., `LoadMovieListRepository`, `SaveMovieDetailsToLocalRepository`). Note: currently concrete classes with no interfaces (known tech debt — see proposal 06)
- **Dependency Injection**: Hilt for DI throughout all layers
- **Coroutine Dispatchers**: Injected via qualifiers (`@IoDispatcher`, `@DefaultDispatcher`, `@MainDispatcher`, `@MainImmediateDispatcher`)
- **Application Scope**: `@ApplicationScope CoroutineScope` with `SupervisorJob()` for app-level work (provided by `CoroutinesModule`)
- **UI State Pattern**: Each feature has a dedicated `UiState` data class managed by ViewModel via `MutableStateFlow` + `.asStateFlow()`
- **Route/Screen Separation**:
  - **Route composables** inject ViewModels and collect state (e.g., `MoviesRoute`)
  - **Screen composables** are pure UI functions receiving state and callbacks (e.g., `MoviesScreen`)
- **Domain Layer Rule**: ViewModels should always go through use cases, not access repositories/managers directly
- **Models Trade-off**: `core:models` contains Room annotations (`@Entity`, `@PrimaryKey`) as a pragmatic trade-off to avoid doubling mapping code

## Build System

### Gradle Commands

```bash
# Build the app
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run all tests
./gradlew test

# Run connected (instrumented) tests on device/emulator
./gradlew connectedAndroidTest

# Clean build
./gradlew clean

# Lint check
./gradlew lint

# Check for dependency updates
./gradlew dependencyUpdates
```

### Custom Gradle Plugins

The project uses convention plugins defined in `build-logic/convention/`:

- `popular.movies.android.application` - Configures Android application modules
- `popular.movies.android.library` - Configures Android library modules
- `popular.movies.android.application.compose` - Adds Compose dependencies
- `popular.movies.android.library.compose` - Compose for library modules
- `popular.movies.android.feature` - Feature module conventions
- `popular.movies.hilt` - Hilt dependency injection setup
- `popular.movies.android.room` - Room database configuration
- `popular.movies.android.firebase` - Firebase integration
- `popular.movies.android.lint` - Lint configuration
- `popular.movies.android.config` - Build config fields

When modifying build configuration, update the convention plugins rather than individual module build files.

### Version Catalog

Dependencies are managed in `gradle/libs.versions.toml` using Gradle version catalogs. This centralizes dependency versions across all modules.

## Configuration

### Required Setup

1. **TMDb API Key**: Add your API key to `local.properties`:
   ```properties
   tmdb_api_key=YOUR_API_KEY
   ```
   Get an API key from: https://www.themoviedb.org/documentation/api

2. **Release Keystore** (for release builds): Configure path in `local.properties`:
   ```properties
   # Default location: ~/.android/release.keystore
   ```

## Technology Stack

- **Language**: Kotlin 2.1+
- **UI**: Jetpack Compose with Material 3 (all features migrated)
- **Architecture**: MVVM + Clean Architecture
- **DI**: Hilt (Dagger) with KSP
- **Networking**: Retrofit + OkHttp + Kotlin Serialization
- **Image Loading**: Coil (with Compose integration)
- **Async**: Kotlin Coroutines + Flow (dispatchers injected via Hilt qualifiers)
- **Database**: Room
- **Preferences**: DataStore
- **Navigation**: Compose Navigation with Material 3 Adaptive Navigation Suite
- **Firebase**: Analytics and Crashlytics
- **Testing**: JUnit, MockK (infrastructure not yet set up)

## Module Dependencies

When adding new dependencies:

1. Features depend on core modules (domain, data, models, ui, resources)
2. Core modules have specific dependencies:
   - `core:domain` depends on `core:data`, `core:models` (Note: ideally domain should not depend on data -- see proposals)
   - `core:data` depends on `core:domain`, `core:models`, `core:network`, `core:database`, `core:datastore`
   - `core:common` provides dispatcher qualifiers, `@ApplicationScope`, and shared utilities
   - `core:datastore` depends on `core:common` (for `@ApplicationScope` qualifier) and `core:models`
   - `core:tmdbApi` is isolated for API definitions
3. Avoid circular dependencies between features
4. Keep feature modules independent from each other

## Navigation

The app uses **Compose Navigation** exclusively (Fragment-based navigation fully removed).

Each feature defines its navigation in a dedicated `navigation/` package with:
- Route constants (e.g., `MOVIES_ROUTE = "movies"`)
- Navigation extension functions (e.g., `NavController.navigateToMovies()`)
- NavGraphBuilder extensions (e.g., `NavGraphBuilder.moviesScreen()`)

## Code Style & Conventions

### Naming Conventions
- ViewModels: Suffixed with `ViewModel` (e.g., `MoviesViewModel`)
- UI State: Suffixed with `UiState` (e.g., `MoviesUiState`)
- Use Cases: Suffixed with `UseCase` (e.g., `MovieListUseCase`)
- Repositories: Suffixed with `Repository` (e.g., `LoadMovieListRepository`)
- Route composables: Suffixed with `Route` (e.g., `MoviesRoute`)
- Screen composables: Suffixed with `Screen` (e.g., `MoviesScreen`)

### Package Structure
- Base package: `com.ant.<layer>.<feature>`
- Feature UI: `com.ant.feature.<feature>.ui`
- Feature navigation: `com.ant.feature.<feature>.navigation`
- Feature components: `com.ant.feature.<feature>.ui.components`

### Compose Best Practices
- Separate Route and Screen composables into different files
- Keep composables pure and stateless when possible
- Use preview annotations for development
- Prefer immutable state with data classes
- Use StateFlow with `.asStateFlow()` for ViewModel state management
- Use `.update{}` for thread-safe state modifications (never `.value =` directly)
- Use `SharedFlow(replay = 0)` for one-off events (navigation, logout), never `StateFlow`
- Side effects (LaunchedEffect for navigation) belong in Route composables only

### Coroutine Best Practices
- Always rethrow `CancellationException` in catch blocks
- Never use `runCatching` with suspend functions (it swallows `CancellationException`)
- Use `@IoDispatcher` / `@DefaultDispatcher` qualifiers instead of hardcoding `Dispatchers.IO`
- Never create ad-hoc `CoroutineScope(dispatcher).launch` — inject `@ApplicationScope` instead
- Use `flowOn(dispatcher)` in use cases, let ViewModels collect in `viewModelScope`
- Cancel previous jobs before re-launching on refresh

## Known Issues & Technical Debt

See `.andrei/proposals/` for detailed review findings and prioritized fix plans.

For architecture documentation, see `documentation/ARCHITECTURE.md`.

## Reference Code
- Check `/home/andrei29/workspace/workspace-android/reference-code` for best practices and other projects
