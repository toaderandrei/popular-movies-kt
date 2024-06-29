package com.ant.models.source.repositories.tvseries

import com.ant.models.entities.TvShow
import com.ant.models.request.RequestType
import com.ant.models.source.datasource.tvseries.TvSeriesListDataSource
import com.ant.models.source.mappers.tvseries.TvSeriesMapper
import com.ant.models.source.repositories.Repository
import com.uwetrottmann.tmdb2.Tmdb
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class LoadTvSeriesListRepository @Inject constructor(
    private val tmdbService: Tmdb,
    private val tvSeriesMapper: TvSeriesMapper,
) : Repository<Repository.Params<RequestType.TvShowRequest>, List<TvShow>> {
    override suspend fun performRequest(params: Repository.Params<RequestType.TvShowRequest>): List<TvShow> {
        return TvSeriesListDataSource(params, tmdbService, tvSeriesMapper).invoke()
    }
}