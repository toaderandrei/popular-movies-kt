# UI Refactoring Plan - Move All UI to Feature Modules

## Current State Analysis

### App Module UI Structure (to be moved)

```
app/src/main/java/com/ant/app/ui/
├── main/
│   ├── MainActivity.kt (legacy - to be deleted)
│   ├── movies/
│   │   ├── MoviesFragment.kt
│   │   ├── MoviesViewModel.kt (old version)
│   │   ├── MoviesEpoxyController.kt
│   │   ├── popular/PopularMoviesFragment.kt, PopularMoviesViewModel.kt
│   │   ├── top/TopRatedMoviesFragment.kt, TopRatedMoviesViewModel.kt
│   │   ├── nowplaying/NowPlayingMoviesFragment.kt, NowPlayingMoviesViewModel.kt
│   │   └── upcoming/UpcomingMoviesFragment.kt, UpcomingMoviesViewModel.kt
│   ├── tvseries/
│   │   ├── TvShowFragment.kt
│   │   ├── TvShowViewModel.kt
│   │   ├── TvShowEpoxyController.kt
│   │   ├── popular/PopularTvShowFragment.kt, PopularTvShowViewModel.kt
│   │   ├── top/TopRatedTvShowFragment.kt, TopRatedTvShowViewModel.kt
│   │   ├── airing_today/AiringTodayTvShowFragment.kt, AiringTodayTvShowViewModel.kt
│   │   └── ontv/OnTvTvShowFragment.kt, OnTvTvShowViewModel.kt
│   ├── favorites/
│   │   ├── FavoritesFragment.kt
│   │   ├── FavoritesViewModel.kt
│   │   └── FavoritesEpoxyController.kt
│   ├── login/
│   │   ├── LoginFragment.kt
│   │   └── LoginViewModelOld.kt
│   ├── profile/
│   │   └── (profile related files)
│   └── base/
│       └── (base classes)
├── details/
│   ├── movies/
│   │   ├── DetailsMoviesFragment.kt
│   │   └── DetailsMovieViewModel.kt
│   └── tvseries/
│       ├── DetailsTvSeriesFragment.kt
│       └── DetailsTvSeriesViewModel.kt
├── settings/
│   ├── SettingsFragment.kt
│   └── SettingsViewModel.kt
├── adapters/
│   ├── MovieListAdapter.kt
│   ├── TvSeriesListAdapter.kt
│   ├── MovieCastsAdapter.kt
│   └── MovieVideosAdapter.kt
├── extensions/
│   ├── ParseMovieListResponse.kt
│   ├── ParseTvShowListResponse.kt
│   ├── ParseFavoriteListResponse.kt
│   ├── ParseMovieStateResponseKtx.kt
│   ├── ViewKtx.kt
│   └── AnalyticsKtx.kt
└── compose/ (KEEP - app-level navigation)
    ├── MainActivityCompose.kt
    ├── app/
    │   ├── MainApp.kt
    │   ├── MainContentState.kt
    │   ├── viewmodel/MainActivityViewModel.kt
    │   └── component/
    │       ├── MoviesBackground.kt
    │       ├── MoviesTopAppBar.kt
    │       ├── MoviesBottomBar.kt
    │       ├── BottomNavigationBar.kt
    │       └── ktx/PopularMoviesNavigationScaffoldUiSuiteKtx.kt
    └── themes/
        └── PopularMoviesBackground.kt
```

## Target Structure

### Features Modules (Compose + Clean Architecture)

Each feature should have ALL its UI code:

```
features/movies/
├── MoviesUiState.kt ✅ (already created)
├── MoviesViewModel.kt ✅ (already migrated to Compose)
├── ui/
│   ├── MoviesRoute.kt ✅ (already created)
│   ├── MoviesScreen.kt ✅ (already created)
│   ├── details/
│   │   ├── MovieDetailsRoute.kt (NEW - from DetailsMoviesFragment)
│   │   ├── MovieDetailsScreen.kt (NEW - migrate from Fragment)
│   │   └── MovieDetailsUiState.kt (NEW)
│   └── components/
│       ├── MovieCard.kt ✅ (already created)
│       ├── MovieCastCard.kt (NEW - from adapter)
│       ├── MovieVideoCard.kt (NEW - from adapter)
│       └── (other movie-specific components)
├── details/
│   └── MovieDetailsViewModel.kt ✅ (exists, needs updating)
├── navigation/
│   └── MoviesNavigation.kt ✅ (already created)
└── legacy/ (temporary - to be migrated then deleted)
    ├── MoviesFragment.kt (from app/ui/main/movies/)
    ├── MoviesEpoxyController.kt (from app/ui/main/movies/)
    └── (subcategory fragments - for reference during migration)

features/tvshow/
├── TvShowUiState.kt (NEW)
├── TvShowViewModel.kt ✅ (exists, needs updating)
├── ui/
│   ├── TvShowRoute.kt (NEW)
│   ├── TvShowScreen.kt (NEW)
│   ├── details/
│   │   ├── TvShowDetailsRoute.kt (NEW)
│   │   ├── TvShowDetailsScreen.kt (NEW)
│   │   └── TvShowDetailsUiState.kt (NEW)
│   └── components/
│       ├── TvShowCard.kt (NEW)
│       └── (other TV show components)
├── details/
│   └── TvShowDetailsViewModel.kt ✅ (exists, needs updating)
├── navigation/
│   └── TvShowNavigation.kt (NEW)
└── legacy/ (temporary)
    ├── TvShowFragment.kt (from app/ui/main/tvseries/)
    ├── TvShowEpoxyController.kt
    └── (subcategory fragments)

features/favorites/
├── FavoritesUiState.kt (NEW)
├── FavoritesViewModel.kt ✅ (exists, needs updating)
├── ui/
│   ├── FavoritesRoute.kt (NEW)
│   ├── FavoritesScreen.kt (NEW)
│   ├── details/
│   │   ├── FavoritesDetailsRoute.kt (NEW)
│   │   └── FavoritesDetailsScreen.kt (NEW)
│   └── components/
│       └── (favorites-specific components)
├── details/
│   └── FavoritesDetailsViewModel.kt ✅ (exists)
├── navigation/
│   └── FavoritesNavigation.kt (NEW)
└── legacy/ (temporary)
    ├── FavoritesFragment.kt (from app/ui/main/favorites/)
    └── FavoritesEpoxyController.kt

features/login/
├── LoginUiState.kt (NEW)
├── LoginViewModel.kt ✅ (exists)
├── ui/
│   ├── LoginRoute.kt (NEW)
│   └── LoginScreen.kt (NEW)
├── navigation/
│   └── LoginNavigation.kt (NEW)
└── legacy/ (temporary)
    ├── LoginFragment.kt (from app/ui/main/login/)
    └── LoginViewModelOld.kt

features/search/
├── SearchUiState.kt (NEW)
├── SearchViewModel.kt ✅ (exists)
├── ui/
│   ├── SearchRoute.kt (NEW)
│   ├── SearchScreen.kt (NEW)
│   └── components/
│       └── SearchBar.kt (NEW)
└── navigation/
    └── SearchNavigation.kt (NEW)
```

### App Module (Only Navigation & Composition)

```
app/src/main/java/com/ant/app/
├── application/
│   └── PopularMoviesApp.kt
├── di/
│   └── (app-level DI modules)
└── ui/
    └── compose/
        ├── MainActivityCompose.kt
        ├── navigation/
        │   ├── AppNavHost.kt (NEW)
        │   └── TopLevelDestination.kt (NEW)
        ├── app/
        │   ├── MainApp.kt
        │   ├── MainContentState.kt
        │   ├── viewmodel/MainActivityViewModel.kt
        │   └── component/
        │       ├── MoviesBackground.kt
        │       ├── MoviesTopAppBar.kt
        │       ├── MoviesBottomBar.kt
        │       └── BottomNavigationBar.kt
        └── themes/
            └── (theme files)
```

## Migration Strategy

### Phase 1: Move Legacy Code to Feature Modules (Keep Functional)

Move existing Fragments, ViewModels, and Epoxy controllers to `legacy/` folders in features.
This keeps the app functional while we migrate.

**Movies Feature:**
1. Move `app/ui/main/movies/*` → `features:movies/legacy/main/`
2. Move `app/ui/details/movies/*` → `features:movies/legacy/details/`
3. Move movie adapters → `features:movies/legacy/adapters/`
4. Update imports and package names

**TV Shows Feature:**
1. Move `app/ui/main/tvseries/*` → `features:tvshow/legacy/main/`
2. Move `app/ui/details/tvseries/*` → `features:tvshow/legacy/details/`
3. Move TV show adapters → `features:tvshow/legacy/adapters/`
4. Update imports and package names

**Favorites Feature:**
1. Move `app/ui/main/favorites/*` → `features:favorites/legacy/main/`
2. Update imports and package names

**Login Feature:**
1. Move `app/ui/main/login/*` → `features:login/legacy/`
2. Update imports and package names

### Phase 2: Convert to Compose (Feature by Feature)

For each feature:
1. ✅ Movies: Already done - use as reference
2. TV Shows: Follow movies pattern
3. Favorites: Follow movies pattern
4. Login: Follow movies pattern
5. Search: Create from scratch in Compose

### Phase 3: Delete Legacy Code

Once Compose versions are complete and tested:
1. Delete `legacy/` folders from all features
2. Delete Epoxy controllers
3. Delete Fragment classes
4. Delete old ViewModels (if duplicates exist)
5. Delete adapters (replaced by Compose)

### Phase 4: Clean App Module

Remove from app module:
- `ui/main/` directory (except MainActivity.kt reference for removal)
- `ui/details/` directory
- `ui/adapters/` directory
- `ui/extensions/` (move to appropriate features or core)
- `ui/settings/` (move to new settings feature or keep if app-level)
- `ui/base/` (delete if unused)
- `ui/insets/` (move to core:ui if reusable)

Keep in app module:
- `ui/compose/` - app-level navigation and composition
- `application/` - app class
- `di/` - app-level DI

## Implementation Steps

### Step 1: Create Legacy Folders Structure
```bash
mkdir -p features/movies/src/main/kotlin/com/ant/feature/movies/legacy
mkdir -p features/tvshow/src/main/kotlin/com/ant/feature/tvshow/legacy
mkdir -p features/favorites/src/main/kotlin/com/ant/feature/favorites/legacy
mkdir -p features/login/src/main/kotlin/com/ant/feature/login/legacy
```

### Step 2: Move Files (maintain functionality)
- Copy (don't delete yet) files to new locations
- Update package names
- Update imports
- Test that app still builds and runs

### Step 3: Implement Compose UI
- Use `features:movies` as template
- Create UiState, Route, Screen, Components
- Implement navigation
- Test each feature

### Step 4: Switch Navigation
- Update app navigation to use Compose screens
- Remove Fragment-based navigation
- Test all navigation flows

### Step 5: Delete Legacy
- Delete `legacy/` folders
- Delete old app/ui code
- Clean up dependencies

## Benefits

1. **Feature Independence**: Each feature is self-contained
2. **Clear Boundaries**: Features only depend on core modules, not on app
3. **Easier Testing**: Feature modules can be tested in isolation
4. **Better Scalability**: New features follow established pattern
5. **Cleaner App Module**: App module only handles composition and navigation
6. **Modern Stack**: 100% Compose, no Fragments, no Epoxy
7. **Maintainability**: Each feature has consistent structure

## Dependencies to Update

Features will need:
```kotlin
// Each feature module
dependencies {
    // Core
    implementation(project(":core:domain"))
    implementation(project(":core:models"))
    implementation(project(":core:ui"))
    implementation(project(":core:resources"))

    // Compose
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    // Image loading
    implementation(libs.coil.kt.compose)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // DO NOT include Epoxy dependencies
}
```

App module will only need:
```kotlin
dependencies {
    // Features
    implementation(project(":features:movies"))
    implementation(project(":features:tvshow"))
    implementation(project(":features:favorites"))
    implementation(project(":features:search"))
    implementation(project(":features:login"))

    // Core
    implementation(project(":core:ui"))
    implementation(project(":core:resources"))

    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.adaptive.navigation)
    implementation(libs.navigation.compose)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Firebase
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
}
```

## Success Criteria

- ✅ All UI code moved out of app module
- ✅ Each feature is self-contained with ui/, navigation/, and ViewModels
- ✅ No Epoxy dependencies anywhere
- ✅ No Fragment classes (except legacy during transition)
- ✅ App module only contains MainActivityCompose and navigation setup
- ✅ All features follow the same consistent structure
- ✅ App builds and runs correctly
- ✅ All navigation flows work
