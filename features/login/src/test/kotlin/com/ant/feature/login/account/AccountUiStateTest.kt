package com.ant.feature.login.account

import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AccountUiStateTest {

    @Test
    fun `default state is loading and not logged in`() {
        val state = AccountUiState()
        assertTrue(state.isLoading)
        assertFalse(state.isLoggedIn)
        assertNull(state.username)
        assertNull(state.sessionId)
        assertFalse(state.isLoggingOut)
        assertFalse(state.logoutSuccess)
        assertNull(state.error)
    }

    @Test
    fun `logged in state has session and username`() {
        val state = AccountUiState(
            isLoading = false,
            isLoggedIn = true,
            username = "testuser",
            sessionId = "abc123",
        )
        assertFalse(state.isLoading)
        assertTrue(state.isLoggedIn)
        assertTrue(state.username == "testuser")
        assertTrue(state.sessionId == "abc123")
    }

    @Test
    fun `logout error preserves login state`() {
        val state = AccountUiState(
            isLoading = false,
            isLoggedIn = true,
            username = "user",
            isLoggingOut = false,
            error = "Logout failed",
        )
        assertTrue(state.isLoggedIn)
        assertTrue(state.error == "Logout failed")
    }
}
