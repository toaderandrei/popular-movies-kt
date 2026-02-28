package com.ant.domain.usecases.favorites

import com.ant.data.repositories.FavoriteRepository
import com.ant.models.model.Result
import com.ant.models.request.FavoriteType
import com.ant.models.session.SessionManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SyncFavoriteToRemoteUseCaseTest {

    private val favoriteRepository = mockk<FavoriteRepository>()
    private val sessionManager = mockk<SessionManager>()

    private val useCase = SyncFavoriteToRemoteUseCase(
        favoriteRepository = favoriteRepository,
        sessionManager = sessionManager,
        dispatcher = Dispatchers.Unconfined,
    )

    @Test
    fun `emits error when no session`() = runTest {
        coEvery { sessionManager.getSessionId() } returns null

        val results = useCase(1L, FavoriteType.MOVIE).toList()

        assertTrue(results[0] is Result.Loading)
        assertTrue(results[1] is Result.Error)
        assertEquals(
            "Login required to sync favorites to TMDb",
            (results[1] as Result.Error).throwable.message,
        )
    }

    @Test
    fun `emits success and updates sync status when remote succeeds`() = runTest {
        coEvery { sessionManager.getSessionId() } returns "session123"
        coEvery { favoriteRepository.syncFavoriteToRemote(any()) } returns true
        coEvery { favoriteRepository.updateSyncStatus(any(), any(), any()) } returns Unit

        val results = useCase(42L, FavoriteType.MOVIE).toList()

        assertTrue(results[0] is Result.Loading)
        assertTrue(results[1] is Result.Success)
        assertEquals(true, (results[1] as Result.Success).data)

        coVerify {
            favoriteRepository.updateSyncStatus(42L, FavoriteType.MOVIE, true)
        }
    }

    @Test
    fun `emits success false when remote returns false`() = runTest {
        coEvery { sessionManager.getSessionId() } returns "session123"
        coEvery { favoriteRepository.syncFavoriteToRemote(any()) } returns false

        val results = useCase(1L, FavoriteType.TV).toList()

        assertTrue(results[1] is Result.Success)
        assertEquals(false, (results[1] as Result.Success).data)

        coVerify(exactly = 0) {
            favoriteRepository.updateSyncStatus(any(), any(), any())
        }
    }
}
