package com.ant.models.source.repositories.tvseries

import com.ant.models.entities.TvShowDetails
import com.ant.models.request.RequestType
import com.ant.models.request.toMovieId
import com.ant.models.source.database.MoviesDb
import com.ant.models.source.datasource.tvseries.TvSeriesDetailsExtendedSummaryDataSource
import com.ant.models.source.datasource.tvseries.TvSeriesDetailsLocalDbDataSource
import com.ant.models.source.mappers.tvseries.TvSeriesDetailsMapper
import com.ant.models.source.repositories.Repository
import com.uwetrottmann.tmdb2.Tmdb
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class LoadTvSeriesDetailsSummaryRepository @Inject constructor(
    private val tmdbService: Tmdb,
    private val moviesDb: MoviesDb,
    private val movieDetailsMapper: TvSeriesDetailsMapper,
) : Repository<Repository.Params<RequestType.TvSeriesRequestDetails>, TvShowDetails> {
    override suspend fun performRequest(params: Repository.Params<RequestType.TvSeriesRequestDetails>): TvShowDetails {
        val movieData = moviesDb.tvSeriesDao().findTvSeriesById(params.request.toMovieId())
        return if (movieData != null) {
            TvSeriesDetailsLocalDbDataSource(movieData, moviesDb).invoke()
        } else TvSeriesDetailsExtendedSummaryDataSource(
            params,
            tmdbService,
            movieDetailsMapper
        ).invoke()
    }
}