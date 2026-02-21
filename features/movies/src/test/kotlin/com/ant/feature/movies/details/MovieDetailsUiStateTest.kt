package com.ant.feature.movies.details

import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MovieDetailsUiStateTest {

    @Test
    fun `default state starts loading with no data`() {
        val state = MovieDetailsUiState()
        assertTrue(state.isLoading)
        assertNull(state.movieDetails)
        assertNull(state.error)
        assertFalse(state.isFavorite)
    }

    @Test
    fun `error state has loading false`() {
        val state = MovieDetailsUiState(
            isLoading = false,
            error = "Network error",
        )
        assertFalse(state.isLoading)
        assertNull(state.movieDetails)
        assertTrue(state.error == "Network error")
    }
}
