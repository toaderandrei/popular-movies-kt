package com.ant.models.entities

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test

class MovieDataTest {

    @Test
    fun `default movie has expected default values`() {
        val movie = MovieData()
        assertEquals(0L, movie.id)
        assertNull(movie.name)
        assertNull(movie.favored)
        assertEquals(false, movie.syncedToRemote)
    }

    @Test
    fun `copy with favored preserves syncedToRemote`() {
        val movie = MovieData(id = 1, syncedToRemote = true)
        val copied = movie.copy(favored = true)
        assertEquals(true, copied.syncedToRemote)
        assertEquals(true, copied.favored)
    }

    @Test
    fun `voteAverageRounded formats to one decimal place`() {
        val movie = MovieData(id = 1, voteAverage = 8.567)
        assertEquals("8.6", movie.voteAverageRounded)
    }

    @Test
    fun `genres joins list with comma separator`() {
        val movie = MovieData(id = 1, _genres = listOf("Action", "Drama", "Sci-Fi"))
        assertEquals("Action, Drama, Sci-Fi", movie.genres)
    }
}
