# Compose Migration - Quick Start Guide

## What We're Doing

**Goal**: Migrate entire app from Epoxy/Fragments to 100% Jetpack Compose, with all UI code in feature modules.

**Strategy**: Build new Compose UI directly, then delete old code. No legacy migration needed.

## Current Status

✅ **Movies Feature**: COMPLETE - Use as reference for everything else
- Full Compose UI with categories, grid, pull-to-refresh
- StateFlow-based state management
- Complete navigation setup
- Reusable components

❌ **To Complete**: TV Shows, Favorites, Search, Login

## How to Proceed

### 1. Use Movies as Template

The movies feature (`features/movies/`) is the perfect reference:

```
features/movies/
├── MoviesUiState.kt           # ✅ Copy this pattern
├── MoviesViewModel.kt         # ✅ Copy state management approach
├── ui/
│   ├── MoviesRoute.kt         # ✅ Copy ViewModel injection
│   ├── MoviesScreen.kt        # ✅ Copy UI structure
│   └── components/
│       └── MovieCard.kt       # ✅ Copy component pattern
└── navigation/
    └── MoviesNavigation.kt    # ✅ Copy navigation setup
```

### 2. Implement Each Feature

Follow this order:

**Priority 1: TV Shows** (~6 hours)
- Copy movies structure
- Replace Movie → TvShow
- Use TvSeriesType enum for categories
- Integrate with TvShowListUseCase

**Priority 2: Favorites** (~3 hours)
- Tabbed layout (Movies/TV Shows tabs)
- Grid for each tab
- Empty state handling

**Priority 3: Search** (~4 hours)
- Search bar with debounce
- Combined results (movies + TV)
- Search history

**Priority 4: Login** (~3 hours)
- Simple form (username/password)
- TMDb authentication

### 3. Delete Old Code

After each feature works in Compose:

```bash
# After TV Shows
rm -rf app/src/main/java/com/ant/app/ui/main/tvseries/
rm -rf app/src/main/java/com/ant/app/ui/details/tvseries/

# After Favorites
rm -rf app/src/main/java/com/ant/app/ui/main/favorites/

# After Login
rm -rf app/src/main/java/com/ant/app/ui/main/login/
```

### 4. Final Cleanup

When all features are done:

```bash
# Delete all old UI
rm -rf app/src/main/java/com/ant/app/ui/main/
rm -rf app/src/main/java/com/ant/app/ui/details/
rm -rf app/src/main/java/com/ant/app/ui/adapters/

# Delete Epoxy module
rm -rf common-ui/epoxy/
rm -rf common-ui/adapters/
rm -rf common-ui/bindings/

# Update settings.gradle (remove those modules)
# Update gradle/libs.versions.toml (remove Epoxy)
# Update build files (remove Epoxy dependencies)
```

## Key Files for Reference

📖 **Read These**:
- `DIRECT_TO_COMPOSE_PLAN.md` - Complete implementation guide
- `features/movies/` - Reference implementation
- `CLAUDE.md` - Architecture documentation

📋 **Planning Docs** (for context):
- `MIGRATION_PLAN.md` - Original Epoxy migration plan
- `REFACTORING_PLAN.md` - Moving UI to features plan
- `IMPLEMENTATION_SUMMARY.md` - What's been completed

## Quick Commands

```bash
# Build movies feature (reference)
./gradlew :features:movies:build

# Build a specific feature you're working on
./gradlew :features:tvshow:build

# Build entire app
./gradlew build

# Run tests
./gradlew test

# Clean build
./gradlew clean build
```

## Pattern Checklist

For each feature, create these files in order:

- [ ] `[Feature]UiState.kt` - Data class with state
- [ ] Update `[Feature]ViewModel.kt` - Add StateFlow management
- [ ] `ui/[Feature]Route.kt` - ViewModel injection
- [ ] `ui/[Feature]Screen.kt` - Main UI composable
- [ ] `ui/components/[Feature]Card.kt` - Card component
- [ ] `navigation/[Feature]Navigation.kt` - Navigation setup
- [ ] Test it works
- [ ] Delete old code from app module

## Success Criteria

When done, you'll have:

✅ All features in Compose
✅ Consistent structure across all features
✅ No Epoxy anywhere
✅ No Fragments
✅ No XML layouts in features
✅ No RecyclerView adapters
✅ App module only contains navigation/composition
✅ Clean, maintainable, modern codebase

## Estimated Time

- TV Shows: 6 hours
- Favorites: 3 hours
- Search: 4 hours
- Login: 3 hours
- Cleanup: 3 hours
- Testing: 3 hours

**Total**: ~22 hours

## Need Help?

1. **Look at movies feature first** - it has everything you need
2. **Check DIRECT_TO_COMPOSE_PLAN.md** - detailed file-by-file guide
3. **Follow the pattern exactly** - don't deviate from movies structure
4. **Test incrementally** - make sure each piece works before moving on

## What NOT to Do

❌ Don't create `legacy/` folders
❌ Don't try to migrate Epoxy gradually
❌ Don't keep old Fragments around
❌ Don't maintain two versions of the same feature
❌ Don't deviate from the movies pattern

## What TO Do

✅ Build Compose versions directly
✅ Follow movies structure exactly
✅ Delete old code once new version works
✅ Test each feature thoroughly
✅ Keep features independent
✅ Use StateFlow for state management

---

**Start Here**: Open `features/movies/` and `DIRECT_TO_COMPOSE_PLAN.md`

**Next Action**: Implement TV Shows feature following movies pattern
