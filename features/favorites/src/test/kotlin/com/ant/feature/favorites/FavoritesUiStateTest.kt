package com.ant.feature.favorites

import com.ant.models.entities.MovieData
import com.ant.models.entities.TvShow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FavoritesUiStateTest {

    @Test
    fun `default state has expected values`() {
        val state = FavoritesUiState()
        assertFalse(state.isLoading)
        assertTrue(state.favoriteMovies.isEmpty())
        assertTrue(state.favoriteTvShows.isEmpty())
        assertEquals(FavoriteTab.MOVIES, state.selectedTab)
        assertNull(state.error)
        assertFalse(state.isRefreshing)
        assertTrue(state.syncingIds.isEmpty())
        assertNull(state.snackbarMessage)
    }

    @Test
    fun `adding syncing id tracks correctly`() {
        val state = FavoritesUiState()
        val updated = state.copy(syncingIds = state.syncingIds + 42L)
        assertTrue(42L in updated.syncingIds)
        assertFalse(99L in updated.syncingIds)
    }

    @Test
    fun `movies and tv shows are stored independently`() {
        val state = FavoritesUiState(
            favoriteMovies = listOf(MovieData(id = 1, name = "Movie")),
            favoriteTvShows = listOf(
                TvShow(
                    id = 2, name = "Show", originalTitle = null,
                    voteCount = null, overview = null, voteAverage = null,
                    backDropPath = null, posterPath = null, originalLanguage = null,
                )
            ),
        )
        assertEquals(1, state.favoriteMovies.size)
        assertEquals(1, state.favoriteTvShows.size)
        assertEquals("Movie", state.favoriteMovies[0].name)
        assertEquals("Show", state.favoriteTvShows[0].name)
    }
}
