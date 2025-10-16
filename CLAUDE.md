# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Popular Movies is a Kotlin-based Android application that showcases popular movies and TV series using The Movie Database (TMDb) API. The app follows Clean Architecture principles with MVVM pattern and is organized into feature modules.

## Architecture

### Clean Architecture Layers

The project follows Clean Architecture with clear separation of concerns:

- **app**: Main application entry point, UI composition, and navigation
  - Uses `MainActivityCompose` with Jetpack Compose
  - Legacy MainActivity is commented out in AndroidManifest.xml

- **features**: Feature modules organized by business domain, following clean separation pattern:
  - `features:movies` - Movie browsing and details (✅ Fully migrated to Compose)
  - `features:tvshow` - TV series browsing and details
  - `features:favorites` - Saved favorites management
  - `features:search` - Search functionality
  - `features:login` - User authentication via TMDb

  **Feature Module Structure** (see `features:movies` as reference):
  ```
  features/[feature-name]/
  ├── [Feature]UiState.kt          # UI state data class
  ├── [Feature]ViewModel.kt        # ViewModel with StateFlow
  ├── ui/
  │   ├── [Feature]Route.kt        # Entry point (ViewModel injection)
  │   ├── [Feature]Screen.kt       # Pure UI composable
  │   └── components/              # Feature-specific components
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
  - `core:common` - Shared utilities
  - `core:ui` - Shared UI components
  - `core:resources` - Shared resources (strings, drawables, etc.)
  - `core:analytics` - Analytics/Firebase integration

- **common-ui**: Reusable UI components (⚠️ Being reorganized for Compose)
  - `common-ui:adapters` - RecyclerView adapters (⚠️ To be removed)
  - `common-ui:epoxy` - Epoxy model configurations (⚠️ To be removed)
  - `common-ui:layouts` - Custom layouts
  - `common-ui:bindings` - Data binding utilities (⚠️ To be removed)

- **build-logic**: Gradle convention plugins for consistent build configuration
  - Uses composite builds pattern (included build)
  - Custom convention plugins define common configurations across modules
  - Based on best practices from Square and idiomatic Gradle patterns

### Data Flow Pattern

1. **UI Layer** (Feature modules) → ViewModels observe UI events
2. **Domain Layer** (Use Cases) → Orchestrates business logic, returns `Flow<Result<T>>`
   - Base class: `UseCase<P, R>` wraps execution in Loading/Success/Error states
   - Executes on specified CoroutineDispatcher
3. **Data Layer** (Repositories) → Handles data sources (remote/local)
   - Base interface: `Repository<P, R>` with `performRequest(params: P): R`
   - Coordinates between network (TMDb API), database (Room), and DataStore

### Key Patterns

- **Result Wrapper**: All use cases return `Flow<Result<T>>` where Result is Loading, Success, or Error
- **Repository Pattern**: Repositories implement single responsibility (e.g., `LoadMovieListRepository`, `SaveMovieDetailsToLocalRepository`)
- **Dependency Injection**: Hilt for DI throughout all layers
- **Coroutines & Flow**: Async operations use Kotlin Coroutines and StateFlow/Flow for reactive updates
- **UI State Pattern**: Each feature has a dedicated `UiState` data class managed by ViewModel
- **Route/Screen Separation**:
  - **Route composables** inject ViewModels and collect state (e.g., `MoviesRoute`)
  - **Screen composables** are pure UI functions receiving state and callbacks (e.g., `MoviesScreen`)

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

- **Language**: Kotlin
- **UI**: Jetpack Compose with Material 3 (movies feature fully migrated)
  - ⚠️ Legacy components still exist: XML layouts, View Binding, Data Binding, Epoxy (being removed)
- **Architecture**: MVVM + Clean Architecture
- **DI**: Hilt (Dagger)
- **Networking**: Retrofit + OkHttp + Kotlin Serialization
- **Image Loading**: Coil (with Compose integration)
- **Async**: Kotlin Coroutines + Flow
- **Database**: Room
- **Preferences**: DataStore
- **Navigation**: Compose Navigation (primary) + Navigation Component (legacy)
- **Firebase**: Analytics and Crashlytics
- **Testing**: JUnit, MockK

## Module Dependencies

When adding new dependencies:

1. Features depend on core modules (domain, data, models, ui, resources)
2. Core modules have specific dependencies:
   - `core:domain` depends on `core:models` only (pure business logic)
   - `core:data` depends on `core:domain`, `core:models`, `core:network`, `core:database`, `core:datastore`
   - `core:tmdbApi` is isolated for API definitions
3. Avoid circular dependencies between features
4. Keep feature modules independent from each other

## Testing

- Unit tests: Located in `src/test/` directories
- Instrumented tests: Located in `src/androidTest/` directories
- Test dependencies: MockK for mocking, JUnit for assertions

Run tests for a specific module:
```bash
./gradlew :features:movies:test
./gradlew :core:domain:test
```

## Navigation

The app uses:
- **Compose Navigation** for Compose screens (primary)
- **Jetpack Navigation Component** for Fragment-based navigation (legacy, being phased out)
- Material 3 Adaptive Navigation Suite for responsive layouts

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
- Separate Route and Screen composables
- Keep composables pure and stateless when possible
- Use preview annotations for development
- Prefer immutable state with data classes
- Use StateFlow for ViewModel state management

## Migration Status & Guidelines

### ✅ Fully Migrated to Compose
- Movies feature (use as reference for other features)

### ⚠️ In Progress
- Other features (tvshow, favorites, search, login)
- Removal of Epoxy dependencies
- Cleanup of legacy UI code

### When Working on Features
1. Follow the `features:movies` structure as the reference pattern
2. Create UiState data class first
3. Implement pure Screen composable
4. Create Route composable for ViewModel injection
5. Set up navigation with extension functions
6. DO NOT use Epoxy for new code
7. Prefer Compose over XML layouts

For detailed migration plan, see `MIGRATION_PLAN.md` and `PROGRESS.md`.
