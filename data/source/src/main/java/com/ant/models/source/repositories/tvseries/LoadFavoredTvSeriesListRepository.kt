package com.ant.models.source.repositories.tvseries

import com.ant.models.entities.TvShow
import com.ant.models.source.database.MoviesDb
import com.ant.models.source.repositories.Repository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class LoadFavoredTvSeriesListRepository @Inject constructor(
    private val moviesDb: MoviesDb,
) : Repository<Boolean, List<TvShow>> {
    override suspend fun fetchData(params: Boolean): List<TvShow> {
        return moviesDb.tvSeriesDao().loadFavoredTvSeriesData(params)
    }
}