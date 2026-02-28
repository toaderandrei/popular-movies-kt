package com.ant.data.repositories.favorites

import com.ant.database.database.MoviesDb
import com.ant.models.request.FavoriteType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class UpdateFavoriteSyncStatusRepository @Inject constructor(
    private val moviesDb: MoviesDb,
) {
    suspend fun updateSyncStatus(id: Long, mediaType: FavoriteType, synced: Boolean) {
        when (mediaType) {
            FavoriteType.MOVIE -> moviesDb.moviesDao().updateSyncStatus(id, synced)
            FavoriteType.TV -> moviesDb.tvSeriesDao().updateSyncStatus(id, synced)
            FavoriteType.PERSON -> { /* Not supported */ }
        }
    }
}
