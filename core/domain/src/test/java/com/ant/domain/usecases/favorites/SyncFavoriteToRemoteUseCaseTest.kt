package com.ant.domain.usecases.favorites

import com.ant.data.repositories.favorites.FavoriteDetailsToRemoteRepository
import com.ant.data.repositories.favorites.UpdateFavoriteSyncStatusRepository
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

    private val favoriteToRemoteRepository = mockk<FavoriteDetailsToRemoteRepository>()
    private val updateSyncStatusRepository = mockk<UpdateFavoriteSyncStatusRepository>(relaxed = true)
    private val sessionManager = mockk<SessionManager>()

    private val useCase = SyncFavoriteToRemoteUseCase(
        favoriteToRemoteRepository = favoriteToRemoteRepository,
        updateSyncStatusRepository = updateSyncStatusRepository,
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
        coEvery { favoriteToRemoteRepository.performRequest(any()) } returns true

        val results = useCase(42L, FavoriteType.MOVIE).toList()

        assertTrue(results[0] is Result.Loading)
        assertTrue(results[1] is Result.Success)
        assertEquals(true, (results[1] as Result.Success).data)

        coVerify {
            updateSyncStatusRepository.updateSyncStatus(42L, FavoriteType.MOVIE, true)
        }
    }

    @Test
    fun `emits success false when remote returns false`() = runTest {
        coEvery { sessionManager.getSessionId() } returns "session123"
        coEvery { favoriteToRemoteRepository.performRequest(any()) } returns false

        val results = useCase(1L, FavoriteType.TV).toList()

        assertTrue(results[1] is Result.Success)
        assertEquals(false, (results[1] as Result.Success).data)

        coVerify(exactly = 0) {
            updateSyncStatusRepository.updateSyncStatus(any(), any(), any())
        }
    }
}
