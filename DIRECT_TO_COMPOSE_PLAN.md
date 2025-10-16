# Direct to Compose Migration Plan

## Strategy: Skip Legacy, Build Compose Directly

**No need for legacy folders or moving old code.** We'll implement everything in Compose following the movies pattern, then delete all the old Epoxy/Fragment code once the new UI is working.

## Current State

✅ **Movies Feature**: Fully implemented in Compose (reference implementation)

❌ **To Implement**: TV Shows, Favorites, Search, Login

❌ **To Delete**: All Epoxy controllers, all Fragments, all adapters, Epoxy module

## Implementation Order

### 1. TV Shows Feature (Priority 1)

**Create these files** (follow movies pattern exactly):

```kotlin
features/tvshow/src/main/kotlin/com/ant/feature/tvshow/
├── TvShowUiState.kt
data class TvShowUiState(
    val isLoading: Boolean = false,
    val tvShows: List<TvShow> = emptyList(),
    val selectedCategory: TvSeriesType = TvSeriesType.POPULAR,
    val error: String? = null,
    val isRefreshing: Boolean = false,
)

├── TvShowViewModel.kt (update existing)
- Add StateFlow<TvShowUiState>
- Integrate with TvShowListUseCase
- Handle category changes and refresh

├── ui/
│   ├── TvShowRoute.kt
│   │   @Composable fun TvShowRoute(
│   │       onNavigateToDetails: (tvShowId: Long) -> Unit,
│   │       viewModel: TvShowViewModel = hiltViewModel()
│   │   )
│   │
│   ├── TvShowScreen.kt
│   │   - Category tabs (Popular, Top Rated, On TV, Airing Today)
│   │   - Grid layout
│   │   - Pull-to-refresh
│   │   - Loading/error/empty states
│   │
│   └── components/
│       └── TvShowCard.kt
│           - Poster image
│           - Rating badge
│           - Title and first air date
│           - Click handling

├── details/
│   ├── TvShowDetailsUiState.kt
│   ├── TvShowDetailsViewModel.kt (update existing)
│   └── ui/
│       ├── TvShowDetailsRoute.kt
│       └── TvShowDetailsScreen.kt
│           - Hero image
│           - Show info (seasons, episodes, status)
│           - Overview
│           - Cast
│           - Similar shows

└── navigation/
    └── TvShowNavigation.kt
        - const val TV_SHOW_ROUTE = "tvshow"
        - const val TV_SHOW_DETAILS_ROUTE = "tvshow/{id}"
        - Extension functions
```

### 2. Favorites Feature (Priority 2)

**Create these files**:

```kotlin
features/favorites/src/main/kotlin/com/ant/feature/favorites/
├── FavoritesUiState.kt
data class FavoritesUiState(
    val isLoading: Boolean = false,
    val favoriteMovies: List<MovieData> = emptyList(),
    val favoriteTvShows: List<TvShow> = emptyList(),
    val selectedTab: FavoriteTab = FavoriteTab.MOVIES,
    val error: String? = null,
)

enum class FavoriteTab { MOVIES, TV_SHOWS }

├── FavoritesViewModel.kt (update existing)
- Add StateFlow<FavoritesUiState>
- Integrate with LoadFavoredMoviesUseCase
- Integrate with LoadFavoredTvSeriesUseCase
- Handle tab changes

├── ui/
│   ├── FavoritesRoute.kt
│   ├── FavoritesScreen.kt
│   │   - Tab row (Movies, TV Shows)
│   │   - Grid for each tab
│   │   - Empty state ("No favorites yet")
│   │   - Remove from favorites action
│   │
│   └── components/
│       └── FavoriteItem.kt (can reuse MovieCard/TvShowCard)

├── details/
│   ├── FavoritesDetailsUiState.kt
│   ├── FavoritesDetailsViewModel.kt (update existing)
│   └── ui/
│       ├── FavoritesDetailsRoute.kt
│       └── FavoritesDetailsScreen.kt

└── navigation/
    └── FavoritesNavigation.kt
```

### 3. Search Feature (Priority 3)

**Create these files**:

```kotlin
features/search/src/main/kotlin/com/ant/feature/search/
├── SearchUiState.kt
data class SearchUiState(
    val query: String = "",
    val isSearching: Boolean = false,
    val movieResults: List<MovieData> = emptyList(),
    val tvShowResults: List<TvShow> = emptyList(),
    val searchHistory: List<String> = emptyList(),
    val error: String? = null,
)

├── SearchViewModel.kt (update existing)
- Add StateFlow<SearchUiState>
- Debounced search
- Search history management

├── ui/
│   ├── SearchRoute.kt
│   ├── SearchScreen.kt
│   │   - Search bar with clear button
│   │   - Search history chips
│   │   - Tabs for Movies/TV Shows results
│   │   - Results grid
│   │   - Empty state ("No results found")
│   │
│   └── components/
│       └── SearchBar.kt

└── navigation/
    └── SearchNavigation.kt
```

### 4. Login Feature (Priority 4)

**Create these files**:

```kotlin
features/login/src/main/kotlin/com/ant/feature/login/
├── LoginUiState.kt
data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val error: String? = null,
)

├── LoginViewModel.kt (update existing)
- Add StateFlow<LoginUiState>
- Integrate with LoginUserToTmDbUseCase

├── ui/
│   ├── LoginRoute.kt
│   └── LoginScreen.kt
│       - Username field
│       - Password field
│       - Login button
│       - Loading indicator
│       - Error messages

└── navigation/
    └── LoginNavigation.kt
```

## Deletion Plan

### Phase 1: After Each Feature is Complete in Compose

Test the Compose version, then delete corresponding old code:

**After TV Shows Compose is done:**
```bash
rm -rf app/src/main/java/com/ant/app/ui/main/tvseries/
rm -rf app/src/main/java/com/ant/app/ui/details/tvseries/
```

**After Favorites Compose is done:**
```bash
rm -rf app/src/main/java/com/ant/app/ui/main/favorites/
```

**After Login Compose is done:**
```bash
rm -rf app/src/main/java/com/ant/app/ui/main/login/
```

### Phase 2: Delete All Remaining Old UI Code

```bash
# Delete all old Fragments and ViewModels
rm -rf app/src/main/java/com/ant/app/ui/main/
rm -rf app/src/main/java/com/ant/app/ui/details/
rm app/src/main/java/com/ant/app/ui/main/MainActivity.kt

# Delete adapters
rm -rf app/src/main/java/com/ant/app/ui/adapters/

# Delete Epoxy controllers (search for files with "EpoxyController")
find app/src/main/java -name "*EpoxyController*" -delete
find features -name "*EpoxyController*" -delete
```

### Phase 3: Remove Epoxy Module Completely

```bash
# Delete the epoxy module
rm -rf common-ui/epoxy/

# Delete adapters and bindings modules (not needed with Compose)
rm -rf common-ui/adapters/
rm -rf common-ui/bindings/

# Delete legacy folders we created
rm -rf features/*/src/main/kotlin/*/legacy/
```

**Update `settings.gradle`**:
```kotlin
// Remove these lines:
include ':common-ui:epoxy'
include ':common-ui:adapters'
include ':common-ui:bindings'
```

**Update `gradle/libs.versions.toml`**:
```toml
# Remove these:
[versions]
epoxy = "4.6.3"  # DELETE

[libraries]
epoxy = { ... }  # DELETE
epoxy-paging = { ... }  # DELETE
epoxy-databinding = { ... }  # DELETE
epoxy-processor = { ... }  # DELETE
```

**Update all `build.gradle.kts` files**:
Remove any dependencies on:
- `project(":common-ui:epoxy")`
- `project(":common-ui:adapters")`
- `project(":common-ui:bindings")`
- `libs.epoxy*`

### Phase 4: Clean Up App Module Dependencies

**app/build.gradle.kts** should only have:
```kotlin
dependencies {
    // Features (all Compose)
    implementation(project(":features:movies"))
    implementation(project(":features:tvshow"))
    implementation(project(":features:favorites"))
    implementation(project(":features:search"))
    implementation(project(":features:login"))

    // Core modules
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":core:resources"))
    implementation(project(":core:analytics"))
    implementation(project(":core:datastore"))

    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.navigation.compose)
    implementation(libs.androidx.compose.material3.adaptive.navigation)
    implementation(libs.androidx.hilt.navigation.compose)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Firebase
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

    // NO Epoxy
    // NO RecyclerView dependencies
    // NO View Binding
    // NO Data Binding
}
```

## Implementation Timeline

- **TV Shows**: 6 hours (list + details)
- **Favorites**: 3 hours (tabs + lists)
- **Search**: 4 hours (search + results)
- **Login**: 3 hours (form)
- **Deletion & Cleanup**: 3 hours
- **Testing & Fixes**: 3 hours

**Total**: ~22 hours

## Key Points

1. ✅ **Movies is the template** - copy structure exactly
2. ❌ **No legacy folders needed** - old code will be deleted
3. ❌ **No Epoxy migration** - going pure Compose
4. ✅ **Each feature is independent** - can be done one at a time
5. ✅ **Test as you go** - verify each feature before moving on
6. ✅ **Clean deletion** - once Compose works, delete all old code

## Success Criteria

- [ ] All features have Compose UI
- [ ] All features follow movies pattern
- [ ] No Epoxy code anywhere
- [ ] No Fragment classes anywhere
- [ ] No XML layouts in features
- [ ] No RecyclerView adapters
- [ ] App module only has compose/ directory
- [ ] All navigation uses Compose Navigation
- [ ] Build succeeds
- [ ] All features work correctly

## Next Action

**Start with TV Shows feature**:
1. Create `TvShowUiState.kt`
2. Update `TvShowViewModel.kt`
3. Create `ui/TvShowRoute.kt`
4. Create `ui/TvShowScreen.kt`
5. Create `ui/components/TvShowCard.kt`
6. Create `navigation/TvShowNavigation.kt`
7. Test it works
8. Delete old `app/ui/main/tvseries/` and `app/ui/details/tvseries/`

Then repeat for Favorites, Search, and Login.
