# App Architecture Documentation

## Overview

The TMDb App follows **Clean Architecture** with **MVVM pattern** and is built entirely with **Jetpack Compose** (movies feature fully migrated, others in progress).

---

## Navigation Structure

### Visual Layout

```
┌─────────────────────────────────────────┐
│  Movies            🔍  👤              │ ← Top App Bar
├─────────────────────────────────────────┤
│                                         │
│                                         │
│         Screen Content Area             │
│         (Movies/TV/Favorites/Settings)  │
│                                         │
│                                         │
├─────────────────────────────────────────┤
│    🎬      📺      ❤️      ⚙️          │ ← Bottom Navigation
│  Movies  TV Shows Favorites Settings   │
└─────────────────────────────────────────┘
```

### Navigation Components

#### **1. Bottom Navigation Bar (4 Primary Tabs)**

**File:** `/core/ui/src/main/kotlin/com/ant/ui/navigation/MainScreenDestination.kt`

```kotlin
enum class MainScreenDestination {
    MOVIES,      // 🎬 Browse movies by category
    TV_SHOW,     // 📺 Browse TV series
    FAVORITES,   // ❤️ Saved favorites
    SETTINGS     // ⚙️ App settings
}
```

**Routes:**
- `movies` → Movies grid with horizontal sections (Popular, Top Rated, Now Playing, Upcoming)
- `tv_show` → TV shows grid (same pattern as movies)
- `favorites` → User's favorite movies and TV shows
- `settings` → App settings and preferences

#### **2. Top App Bar (Persistent Actions)**

**File:** `/app/src/main/java/com/ant/app/ui/compose/app/component/PopularMoviesTopAppBar.kt`

**Components:**
- **Title**: Shows current screen name (e.g., "Movies", "TV Shows")
- **Search Icon** (🔍): Opens search screen
- **Account Icon** (👤): Opens account/profile screen

**Additional Routes (not in bottom nav):**
- `search` → Search movies and TV shows
- `account` → User profile/login screen

---

## App Structure

### File Organization

```
app/
├── src/main/java/com/ant/app/
│   └── ui/compose/app/
│       ├── MainApp.kt                      # Root composable, auth flow
│       ├── MainContentState.kt             # Navigation state management
│       ├── component/
│       │   ├── PopularMoviesTopAppBar.kt   # Top bar with Search & Account
│       │   └── ktx/
│       │       ├── PopularMoviesNavigationSuiteScaffold.kt
│       │       └── isTopLevelDestinationInHierarchy.kt
│       ├── themes/
│       │   ├── Color.kt
│       │   ├── Theme.kt
│       │   └── GradientBackground.kt
│       └── viewmodel/
│           └── MainActivityViewModel.kt    # Authentication state
│
core/
├── ui/
│   └── navigation/
│       └── MainScreenDestination.kt        # Navigation destinations
│
features/
├── movies/                                  # ✅ Fully Compose
│   ├── MoviesUiState.kt
│   ├── MoviesViewModel.kt
│   ├── ui/
│   │   ├── MoviesRoute.kt                 # ViewModel injection
│   │   ├── MoviesScreen.kt                # Pure UI (vertical + horizontal scroll)
│   │   └── components/
│   │       ├── MovieCard.kt               # Grid view card (legacy)
│   │       ├── MoviePosterCard.kt         # Horizontal scroll poster
│   │       └── MovieSectionRow.kt         # Section with horizontal scroll
│   └── navigation/
│       └── MoviesNavigation.kt
│
├── tvshow/                                 # 🚧 In Progress
├── favorites/                              # 🚧 In Progress
├── search/                                 # 🚧 In Progress
└── login/                                  # 🚧 In Progress
```

---

## Feature Architecture Pattern

### Standard Feature Structure (Following Movies)

```
features/[feature-name]/
├── [Feature]UiState.kt              # UI state data class
├── [Feature]ViewModel.kt            # ViewModel with StateFlow
├── ui/
│   ├── [Feature]Route.kt           # Entry point (ViewModel injection)
│   ├── [Feature]Screen.kt          # Pure UI composable
│   └── components/                 # Feature-specific components
│       ├── [Feature]Card.kt
│       └── [Feature]Section.kt
└── navigation/
    └── [Feature]Navigation.kt      # Navigation setup & extensions
```

### Example: Movies Feature

**1. UI State** (`MoviesUiState.kt`)
```kotlin
data class MoviesUiState(
    val isLoading: Boolean = false,
    val movieSections: Map<MovieType, MovieSection> = emptyMap(),
    val error: String? = null,
    val isRefreshing: Boolean = false,
)

data class MovieSection(
    val category: MovieType,
    val movies: List<MovieData> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)
```

**2. ViewModel** (`MoviesViewModel.kt`)
```kotlin
@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val movieListUseCase: MovieListUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MoviesUiState())
    val uiState: StateFlow<MoviesUiState> = _uiState.asStateFlow()

    init {
        loadAllCategories() // Loads all 4 categories at once
    }
}
```

**3. Route** (`MoviesRoute.kt`)
```kotlin
@Composable
fun MoviesRoute(
    onNavigateToDetails: (movieId: Long) -> Unit,
    onNavigateToCategory: (MovieType) -> Unit,
    viewModel: MoviesViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MoviesScreen(
        uiState = uiState,
        onMovieClick = onNavigateToDetails,
        onMoreClick = onNavigateToCategory,
        onRefresh = viewModel::refresh,
    )
}
```

**4. Screen** (`MoviesScreen.kt`)
```kotlin
@Composable
fun MoviesScreen(
    uiState: MoviesUiState,
    onMovieClick: (movieId: Long) -> Unit,
    onMoreClick: (MovieType) -> Unit,
    onRefresh: () -> Unit,
) {
    // Pure UI - vertical LazyColumn with horizontal LazyRow sections
    LazyColumn {
        items(sections) { section ->
            MovieSectionRow(
                title = section.title,
                movies = section.movies,
                onMovieClick = onMovieClick,
                onMoreClick = onMoreClick
            )
        }
    }
}
```

**5. Components** (`MovieSectionRow.kt`, `MoviePosterCard.kt`)
- Reusable UI components
- Horizontal scrolling poster rows
- Section headers with "MORE" links

---

## Data Flow

### Standard Flow Pattern

```
User Interaction
    ↓
Route (collects state)
    ↓
ViewModel (business logic)
    ↓
Use Case (domain layer)
    ↓
Repository (data layer)
    ↓
Network/Database
    ↓
Flow<Result<T>> ← Result wrapper (Loading/Success/Error)
    ↓
ViewModel (updates StateFlow)
    ↓
Route (observes StateFlow)
    ↓
Screen (renders UI)
```

### Result Wrapper Pattern

All use cases return `Flow<Result<T>>`:
```kotlin
sealed class Result<out T> {
    object Loading : Result<Nothing>()
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val throwable: Throwable) : Result<Nothing>()
}
```

---

## UI Design Pattern

### Movies/TV Shows Screen Layout

Based on `/pictures/ui_app.png`:

```
┌─────────────────────────────────┐
│ Movies        🔍  👤            │ ← Top App Bar
├─────────────────────────────────┤
│                                 │
│ Popular              MORE →     │ ← Section Header
│ [Poster] [Poster]    →          │ ← Horizontal Scroll
│                                 │
│ Top Rated            MORE →     │
│ [Poster] [Poster]    →          │
│                                 │
│ Now Playing          MORE →     │
│ [Poster] [Poster]    →          │
│         ↓ Vertical Scroll       │
├─────────────────────────────────┤
│ 🎬  📺  ❤️  ⚙️                 │ ← Bottom Nav
└─────────────────────────────────┘
```

**Key Features:**
- Vertical scroll for categories
- Horizontal scroll within each category
- Poster-only cards (2:3 aspect ratio)
- Section headers with "MORE" button
- Clean, minimal design

---

## Navigation Graph

```
Root (NavHost)
├── Authentication Flow
│   └── login → Main
│
└── Main Flow (Bottom Nav + Top Bar)
    ├── movies (Bottom Nav)
    ├── tv_show (Bottom Nav)
    ├── favorites (Bottom Nav)
    ├── settings (Bottom Nav)
    ├── search (Top Bar Action)
    └── account (Top Bar Action)
```

**Implementation:**
- File: `/app/src/main/java/com/ant/app/ui/compose/app/MainApp.kt`
- Lines 168-222: Navigation setup

---

## Technology Stack

### UI Layer
- **Jetpack Compose** (100% Compose for Movies, migrating others)
- **Material 3** design components
- **Coil** for image loading
- **Navigation Compose** for screen navigation

### State Management
- **StateFlow** for reactive UI state
- **Kotlin Coroutines** for async operations
- **Flow** for data streams

### Architecture
- **MVVM** pattern
- **Clean Architecture** (Domain, Data, UI layers)
- **Hilt** for dependency injection

### Network & Data
- **Retrofit** + OkHttp for API calls
- **Room** for local database
- **DataStore** for preferences
- **Kotlin Serialization** for JSON

---

## Preview Support

### Compose Preview Pattern

For components with `AsyncImage` (Coil):

```kotlin
@Composable
fun MovieCard(...) {
    val isInPreview = LocalInspectionMode.current

    if (isInPreview) {
        // Use static image from resources
        Image(painter = painterResource(R.drawable.placeholder_movie_item_image))
    } else {
        // Use AsyncImage for runtime
        AsyncImage(model = imageUrl)
    }
}
```

**Required Dependencies:**
```kotlin
dependencies {
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
```

---

## Key Files Reference

### Navigation
- `MainScreenDestination.kt` - Bottom nav destinations
- `MainApp.kt` - Root navigation setup
- `MainContentState.kt` - Navigation state
- `PopularMoviesTopAppBar.kt` - Top bar component

### Theme
- `Theme.kt` - Material 3 theme
- `Color.kt` - Color palette
- `GradientBackground.kt` - Background gradients

### Feature Example (Movies)
- `MoviesUiState.kt` - State model
- `MoviesViewModel.kt` - Business logic
- `MoviesRoute.kt` - ViewModel injection
- `MoviesScreen.kt` - UI implementation
- `MoviesNavigation.kt` - Navigation setup

---

## Migration Status

### ✅ Completed
- Movies feature (fully Compose)
- Bottom navigation (4 tabs)
- Top app bar (Search + Account)
- Navigation structure
- Preview support for AsyncImage

### 🚧 In Progress
- TV Shows feature
- Favorites feature
- Search feature
- Settings feature
- Account/Profile feature

### 📋 Planned
- Movie details screen
- TV show details screen
- Remove legacy XML layouts
- Remove Epoxy dependencies
- Comprehensive testing

---

## Best Practices

### When Adding New Features

1. Follow the Movies feature structure exactly
2. Create UiState data class first
3. Implement pure Screen composable
4. Create Route composable for ViewModel injection
5. Set up navigation with extension functions
6. Use StateFlow for state management
7. Handle Loading/Success/Error states
8. Add preview support for components

### Component Design
- Keep composables pure and stateless when possible
- Separate Route (stateful) from Screen (stateless)
- Use preview annotations for development
- Prefer immutable state with data classes
- Use Material 3 components

### State Management
- Use StateFlow in ViewModels
- Collect state with `collectAsStateWithLifecycle()`
- Handle all Result states (Loading/Success/Error)
- Provide meaningful error messages
- Support pull-to-refresh where applicable

---

## References

- **CLAUDE.md** - Project overview and conventions
- **PROGRESS.md** - Migration progress tracking
- **MIGRATION_PLAN.md** - Detailed migration plan
- **ui_app.png** - UI design reference
