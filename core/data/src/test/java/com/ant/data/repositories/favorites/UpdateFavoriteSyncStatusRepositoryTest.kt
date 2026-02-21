package com.ant.data.repositories.favorites

import com.ant.database.dao.MoviesDao
import com.ant.database.dao.TvSeriesDao
import com.ant.database.database.MoviesDb
import com.ant.models.request.FavoriteType
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class UpdateFavoriteSyncStatusRepositoryTest {

    private val moviesDao = mockk<MoviesDao>(relaxed = true)
    private val tvSeriesDao = mockk<TvSeriesDao>(relaxed = true)
    private val moviesDb = mockk<MoviesDb> {
        every { moviesDao() } returns moviesDao
        every { tvSeriesDao() } returns tvSeriesDao
    }

    private val repository = UpdateFavoriteSyncStatusRepository(moviesDb)

    @Test
    fun `updates movie sync status via movies dao`() = runTest {
        repository.updateSyncStatus(1L, FavoriteType.MOVIE, true)
        coVerify { moviesDao.updateSyncStatus(1L, true) }
    }

    @Test
    fun `updates tv show sync status via tv series dao`() = runTest {
        repository.updateSyncStatus(2L, FavoriteType.TV, false)
        coVerify { tvSeriesDao.updateSyncStatus(2L, false) }
    }

    @Test
    fun `person type does not crash`() = runTest {
        repository.updateSyncStatus(3L, FavoriteType.PERSON, true)
        coVerify(exactly = 0) { moviesDao.updateSyncStatus(any(), any()) }
        coVerify(exactly = 0) { tvSeriesDao.updateSyncStatus(any(), any()) }
    }
}
