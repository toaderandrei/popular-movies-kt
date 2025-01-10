package com.ant.data.repositories.tvseries

import com.ant.data.repositories.Repository
import com.ant.models.entities.TvShow
import com.ant.models.request.RequestType
import com.ant.network.datasource.tvseries.TvSeriesListDataSource
import com.ant.network.mappers.tvseries.TvSeriesMapper
import com.uwetrottmann.tmdb2.Tmdb
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class LoadTvSeriesListRepository @Inject constructor(
    private val tmdbService: Tmdb,
    private val tvSeriesMapper: TvSeriesMapper,
) : Repository<RequestType.TvShowRequest, List<TvShow>> {
    override suspend fun performRequest(params: RequestType.TvShowRequest): List<TvShow> {
        return TvSeriesListDataSource(
            params,
            tmdbService,
            tvSeriesMapper
        ).invoke()
    }
}