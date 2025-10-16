# Navigation Guide - Quick Reference

## Visual Layout

```
┌─────────────────────────────────────────┐
│  Movies            🔍  👤              │ ← Top App Bar
│  [Dynamic Title]  Search Account       │
├─────────────────────────────────────────┤
│                                         │
│  Popular                    MORE →     │ ← Section Header
│  [🎬] [🎬] [🎬] ───→                  │ ← Horizontal Scroll
│                                         │
│  Top Rated                  MORE →     │
│  [🎬] [🎬] [🎬] ───→                  │
│                                         │
│  Now Playing                MORE →     │
│  [🎬] [🎬] [🎬] ───→                  │
│         ↓ Vertical Scroll              │
├─────────────────────────────────────────┤
│    🎬        📺        ❤️        ⚙️    │ ← Bottom Nav
│  Movies   TV Shows  Favorites Settings │
└─────────────────────────────────────────┘
```

## Navigation Structure

### Bottom Navigation (Primary - 4 Tabs)

| Icon | Label | Route | Purpose |
|------|-------|-------|---------|
| 🎬 | Movies | `movies` | Browse movies by category |
| 📺 | TV Shows | `tv_show` | Browse TV series |
| ❤️ | Favorites | `favorites` | Saved favorites |
| ⚙️ | Settings | `settings` | App settings |

### Top App Bar (Secondary Actions)

| Icon | Action | Route | Purpose |
|------|--------|-------|---------|
| 🔍 | Search | `search` | Search movies/TV |
| 👤 | Account | `account` | Profile/Login |

## File Locations

### Navigation Configuration
```
core/ui/navigation/
└── MainScreenDestination.kt        # Bottom nav destinations

app/ui/compose/app/
├── MainApp.kt                      # Root navigation setup
├── MainContentState.kt             # Navigation state
└── component/
    └── PopularMoviesTopAppBar.kt   # Top bar component
```

### Feature Screens
```
features/
├── movies/ui/MoviesScreen.kt       # ✅ Complete
├── tvshow/ui/TvShowScreen.kt       # 🚧 In Progress
├── favorites/ui/FavoritesScreen.kt # 🚧 In Progress
├── search/ui/SearchScreen.kt       # 🚧 In Progress
└── login/ui/LoginScreen.kt         # 🚧 In Progress
```

## How Navigation Works

### 1. User Taps Bottom Nav Tab
```kotlin
Bottom Tab Click
    ↓
MainContentState.navigateToDestination(destination)
    ↓
NavController.navigate(destination.route)
    ↓
NavHost displays corresponding screen
```

### 2. User Taps Top Bar Icon
```kotlin
Top Bar Icon Click (Search/Account)
    ↓
MainNavController.navigate("search" or "account")
    ↓
Screen opens as overlay/destination
```

## Code Examples

### Adding a New Screen

**1. Define in MainScreenDestination.kt (if bottom nav):**
```kotlin
enum class MainScreenDestination {
    // ... existing
    NEW_FEATURE(
        route = "new_feature",
        titleTextId = R.string.new_feature,
        selectedIcon = Icons.Filled.NewIcon,
        unselectedIcon = Icons.Outlined.NewIcon
    )
}
```

**2. Add to NavHost in MainApp.kt:**
```kotlin
NavHost(...) {
    // ... existing screens

    newFeatureScreen(
        onNavigateToDetails = { /* ... */ }
    )
}
```

**3. Update MainContentState.kt:**
```kotlin
val currentMainScreenDestinations: MainScreenDestination?
    @Composable get() = when (currentDestination?.route) {
        // ... existing
        MainScreenDestination.NEW_FEATURE.route -> MainScreenDestination.NEW_FEATURE
        else -> null
    }
```

### Navigating Programmatically

**From ViewModel (not recommended):**
Use navigation callbacks instead

**From Composable (via callback):**
```kotlin
MoviesScreen(
    onMovieClick = { movieId ->
        navController.navigate("movie_details/$movieId")
    }
)
```

## Design Principles

✅ **DO:**
- Keep primary navigation (most-used) in bottom bar
- Use top bar for secondary actions (search, account)
- Limit bottom nav to 3-5 items
- Use clear, descriptive labels
- Provide both filled and outlined icon variants

❌ **DON'T:**
- Put too many items in bottom nav (cramped)
- Mix different navigation patterns
- Use confusing icons
- Forget to handle back navigation

## Navigation Flow Diagram

```
App Launch
    ↓
MainActivityViewModel checks auth
    ↓
┌─────────────┬──────────────┐
│ Not Logged  │ Logged In    │
│     ↓       │      ↓       │
│  Login      │   MainApp    │
│  Screen     │      ↓       │
│     ↓       │   Movies     │
│  Success    │   (default)  │
│     ↓       │      ↓       │
└─────────────┴──────────────┘
         ↓
    MainApp with Bottom Nav
         ↓
    User navigates between:
    - Movies
    - TV Shows
    - Favorites
    - Settings
    - Search (from top bar)
    - Account (from top bar)
```

## Testing Navigation

### Manual Testing Checklist
- [ ] Bottom nav tabs switch screens correctly
- [ ] Top bar title updates based on current screen
- [ ] Search icon opens search screen
- [ ] Account icon opens profile/login
- [ ] Back button works correctly
- [ ] State is preserved when switching tabs
- [ ] Deep links work (if applicable)

### Key Files to Test
1. `MainApp.kt` - Overall navigation
2. `MainContentState.kt` - State management
3. `PopularMoviesTopAppBar.kt` - Top bar actions
4. Each feature's `*Screen.kt` - Individual screens

## Troubleshooting

### Top Bar Not Showing
- Check `Scaffold` in MainApp.kt has `topBar` parameter
- Verify `PopularMoviesTopAppBar` is properly imported
- Check windowInsets handling

### Bottom Nav Not Working
- Verify destinations are in `MainScreenDestination.kt`
- Check `MainContentState.topLevelDestinations` includes all tabs
- Ensure NavHost has routes for all destinations

### Navigation Not Switching Screens
- Check route strings match exactly
- Verify NavHost startDestination is valid
- Check navigation lambda callbacks are wired up

## References

- [Material Design - Bottom Navigation](https://m3.material.io/components/navigation-bar)
- [Material Design - Top App Bar](https://m3.material.io/components/top-app-bar)
- [Jetpack Compose Navigation](https://developer.android.com/jetpack/compose/navigation)
- **ARCHITECTURE.md** - Complete architecture documentation
- **PROGRESS.md** - Migration progress
