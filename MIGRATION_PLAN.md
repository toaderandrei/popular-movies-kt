# Compose Migration & Architecture Reorganization Plan

## Overview
This document outlines the plan to:
1. Fully migrate from Epoxy to Jetpack Compose
2. Reorganize features to match the reference architecture pattern from myhale-master

## Current State Analysis

### What Exists
- ✅ Basic Compose screens in features (MoviesScreen, TvShowScreen, etc.)
- ✅ ViewModels in each feature
- ✅ Jetpack Compose dependencies configured
- ✅ MainActivityCompose as entry point
- ❌ Incomplete Compose UI implementations
- ❌ Epoxy module still present (`common-ui/epoxy`)
- ❌ Legacy XML layouts in app module
- ❌ Missing proper navigation structure per feature
- ❌ No clear separation of UI and navigation concerns

### Reference Architecture Pattern (myhale-master)

Each feature module follows this structure:
```
feature/[feature-name]/
├── src/
│   ├── androidMain/kotlin/
│   │   └── [package]/
│   │       ├── ui/
│   │       │   └── [Feature]Screen.kt     // Composable UI
│   │       └── navigation/
│   │           └── [Feature]Navigation.kt  // Navigation setup
│   ├── commonMain/kotlin/
│   │   └── [package]/
│   │       ├── [Feature]ViewModel.kt       // ViewModel
│   │       ├── [Feature]UiState.kt         // UI State
│   │       └── di/
│   │           └── [Feature]Module.kt      // DI setup
│   └── commonTest/kotlin/                  // Tests
```

Key principles:
- **Separation of concerns**: UI, navigation, business logic, and DI are clearly separated
- **Route composables**: Entry point composables that wire ViewModel to UI (e.g., `LoginScreenRoute`)
- **Screen composables**: Pure UI composables that take state and callbacks
- **Navigation files**: Define navigation destinations and graphs
- **DI modules**: Feature-specific dependency injection configuration

## Migration Steps

### Phase 1: Feature Structure Reorganization

#### Step 1.1: Restructure each feature module
For each feature (movies, tvshow, favorites, search, login):

1. Create proper package structure:
   ```
   src/main/kotlin/com/ant/feature/[name]/
   ├── ui/
   │   ├── [Feature]Screen.kt      // Main screen composable
   │   ├── [Feature]Route.kt       // Route composable (entry point)
   │   └── components/             // Reusable components
   ├── navigation/
   │   └── [Feature]Navigation.kt  // Navigation setup
   ├── [Feature]ViewModel.kt       // Keep at feature root
   └── [Feature]UiState.kt         // UI state data class
   ```

2. Separate Route and Screen composables:
   - **Route**: Injects ViewModel, collects state, handles navigation
   - **Screen**: Pure UI function, receives state and callbacks

#### Step 1.2: Create UI State classes
For each feature, create explicit `UiState` data classes:
```kotlin
data class MoviesUiState(
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val error: String? = null,
    val selectedCategory: MovieCategory = MovieCategory.POPULAR
)
```

#### Step 1.3: Implement Navigation files
Create navigation setup for each feature:
```kotlin
// MoviesNavigation.kt
const val MOVIES_ROUTE = "movies"

fun NavController.navigateToMovies(navOptions: NavOptions? = null) {
    navigate(MOVIES_ROUTE, navOptions)
}

fun NavGraphBuilder.moviesScreen(
    onNavigateToDetails: (Int) -> Unit,
    onNavigateBack: () -> Unit,
) {
    composable(route = MOVIES_ROUTE) {
        MoviesRoute(
            onNavigateToDetails = onNavigateToDetails,
            onNavigateBack = onNavigateBack,
        )
    }
}
```

### Phase 2: Implement Complete Compose UI

#### Step 2.1: Movies Feature
- Replace placeholder UI with full implementation
- Implement movie list with categories (Popular, Top Rated, Upcoming, Now Playing)
- Create movie card composables
- Add lazy column/grid layouts
- Implement pull-to-refresh
- Add loading and error states

#### Step 2.2: Movie Details Feature
- Create detailed movie screen with:
  - Movie poster and backdrop
  - Title, rating, release date
  - Overview/synopsis
  - Cast and crew
  - Similar movies
  - Favorite button
- Implement scroll behavior

#### Step 2.3: TV Shows Feature
- Mirror movies feature structure
- Implement TV series list and details
- Add season/episode information

#### Step 2.4: Favorites Feature
- Create favorites screen with tabs:
  - Favorite Movies
  - Favorite TV Shows
- Implement empty state
- Add remove functionality

#### Step 2.5: Search Feature
- Implement search UI with:
  - Search bar
  - Search history
  - Results list (movies + TV shows)
  - Filter options

#### Step 2.6: Login Feature
- Implement TMDb login flow
- Create login form
- Add authentication state handling
- Implement logout functionality

### Phase 3: Remove Epoxy Dependencies

#### Step 3.1: Identify Epoxy usage
- Scan for any remaining Epoxy controllers, models, or extensions
- Document what each Epoxy component does

#### Step 3.2: Create Compose equivalents
- Replace `TmdbCarousel` with Compose horizontal pager/carousel
- Replace Epoxy RecyclerView items with Compose LazyColumn/LazyRow items
- Migrate any custom Epoxy models to Composables

#### Step 3.3: Remove Epoxy module
1. Remove all dependencies on `:common-ui:epoxy`
2. Delete the `common-ui/epoxy` module
3. Update `settings.gradle` to remove epoxy module
4. Remove epoxy from version catalog

#### Step 3.4: Remove legacy XML layouts
- Delete fragment XML layouts from app module
- Remove view binding references
- Remove data binding if not needed elsewhere

### Phase 4: Shared UI Components

#### Step 4.1: Create Compose UI components module
Reorganize `common-ui` to focus on Compose:
```
common-ui/
├── components/         // Shared composables
│   ├── MovieCard.kt
│   ├── TvShowCard.kt
│   ├── LoadingState.kt
│   ├── ErrorState.kt
│   └── EmptyState.kt
├── theme/             // Material3 theme
└── utils/             // Compose utilities
```

#### Step 4.2: Remove unnecessary modules
- Remove `common-ui:adapters` (RecyclerView adapters not needed)
- Keep `common-ui:layouts` only if needed for Compose custom layouts
- Remove `common-ui:bindings` (data binding not needed with Compose)

### Phase 5: Update Build Configuration

#### Step 5.1: Update feature build files
Ensure all feature modules use:
```kotlin
plugins {
    alias(libs.plugins.popular.movies.android.feature)
    alias(libs.plugins.popular.movies.android.library.compose)
}
```

#### Step 5.2: Update dependencies
Remove from all modules:
- Epoxy dependencies
- RecyclerView (if not needed)
- View Binding
- Data Binding
- Fragment dependencies (if migrated)

Add to features:
- Navigation Compose
- Hilt Navigation Compose
- Lifecycle Compose

#### Step 5.3: Update version catalog
Remove from `gradle/libs.versions.toml`:
```toml
[versions]
epoxy = "4.6.3"  # REMOVE

[libraries]
epoxy = { ... }  # REMOVE
epoxy-paging = { ... }  # REMOVE
epoxy-databinding = { ... }  # REMOVE
epoxy-processor = { ... }  # REMOVE
```

### Phase 6: App Module Cleanup

#### Step 6.1: Update MainActivityCompose
- Ensure complete navigation graph
- Wire all features into nav host
- Implement bottom navigation or drawer
- Handle deep links

#### Step 6.2: Remove legacy MainActivity
- Delete commented-out MainActivity code
- Remove fragment-based navigation
- Remove XML navigation graphs

#### Step 6.3: Clean app dependencies
- Remove unused view-based libraries
- Keep only Compose dependencies

### Phase 7: Testing & Documentation

#### Step 7.1: Update tests
- Write/update screenshot tests for new Compose UI
- Add UI state tests
- Test navigation flows

#### Step 7.2: Update CLAUDE.md
- Remove references to Epoxy
- Update architecture section
- Document new feature structure
- Update technology stack

## Implementation Order

### Priority 1 (Core Features)
1. Movies feature complete UI
2. Movie details screen
3. Navigation between movies and details

### Priority 2 (Supporting Features)
4. TV Shows feature
5. Favorites feature
6. Search feature

### Priority 3 (Authentication)
7. Login feature

### Priority 4 (Cleanup)
8. Remove Epoxy module
9. Remove legacy XML layouts
10. Clean up common-ui modules
11. Update documentation

## Success Criteria

- ✅ No Epoxy dependencies in any module
- ✅ All features use 100% Jetpack Compose
- ✅ No XML layouts in feature modules
- ✅ Each feature has proper navigation setup
- ✅ UI State classes for all features
- ✅ Separation of Route and Screen composables
- ✅ All features follow consistent structure
- ✅ App builds and runs without Epoxy
- ✅ Documentation updated

## Notes

- **Backwards Compatibility**: Since MainActivity is already commented out, no need to maintain old UI
- **Incremental Migration**: Can be done feature by feature
- **Testing**: Test each feature after migration before moving to next
- **Reference**: Use myhale-master as reference for structure and patterns