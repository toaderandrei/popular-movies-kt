package com.ant.data.repositories.tvseries

import com.ant.models.entities.TvShowDetails
import com.ant.database.database.MoviesDb
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class SaveTvSeriesDetailsRepository @Inject constructor(
    private val moviesDb: MoviesDb,
) {
    suspend fun performRequest(params: TvShowDetails) {
        moviesDb.tvSeriesDao().insert(params.tvSeriesData.copy(favored = true, syncedToRemote = false))
    }
}