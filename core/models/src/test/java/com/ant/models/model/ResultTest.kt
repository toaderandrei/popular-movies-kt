package com.ant.models.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ResultTest {

    @Test
    fun `Success isSuccess returns true`() {
        val result: Result<String> = Result.Success("data")
        assertTrue(result.isSuccess)
        assertFalse(result.isFailure)
        assertFalse(result.isLoading)
    }

    @Test
    fun `Error isFailure returns true`() {
        val result: Result<String> = Result.Error(RuntimeException("fail"))
        assertTrue(result.isFailure)
        assertFalse(result.isSuccess)
    }

    @Test
    fun `Loading isLoading returns true`() {
        val result: Result<String> = Result.Loading
        assertTrue(result.isLoading)
        assertFalse(result.isSuccess)
        assertFalse(result.isFailure)
    }

    @Test
    fun `get returns data for Success`() {
        val result: Result<String> = Result.Success("hello")
        assertEquals("hello", result.get())
    }

    @Test
    fun `get returns null for Error`() {
        val result: Result<String> = Result.Error(RuntimeException())
        assertNull(result.get())
    }

    @Test
    fun `getErrorOrNull returns throwable for Error`() {
        val exception = RuntimeException("test error")
        val result: Result<String> = Result.Error(exception)
        assertEquals(exception, result.getErrorOrNull())
    }

    @Test
    fun `fold dispatches to correct branch`() {
        val success: Result<Int> = Result.Success(42)
        val folded = success.fold(
            onSuccess = { "got $it" },
            onLoading = { "loading" },
            onFailure = { "error" },
        )
        assertEquals("got 42", folded)
    }
}
