package com.ant.data.repositories.tvseries

import com.ant.database.database.MoviesDb
import com.ant.models.entities.TvShowDetails
import com.ant.models.request.RequestType
import com.ant.models.request.toMovieId
import com.ant.network.datasource.tvseries.TvSeriesDetailsExtendedSummaryDataSource
import com.ant.network.datasource.tvseries.TvSeriesDetailsLocalDbDataSource
import com.ant.network.mappers.tvseries.TvSeriesDetailsMapper
import com.uwetrottmann.tmdb2.Tmdb
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadTvSeriesDetailsSummaryRepository @Inject constructor(
    private val tmdbService: Tmdb,
    private val moviesDb: MoviesDb,
    private val movieDetailsMapper: TvSeriesDetailsMapper,
) {
    suspend fun performRequest(params: RequestType.TvSeriesRequestDetails): TvShowDetails {
        val movieData = moviesDb.tvSeriesDao().findTvSeriesById(params.toMovieId())
        return if (movieData != null) {
            TvSeriesDetailsLocalDbDataSource(
                movieData,
                moviesDb
            ).invoke()
        } else TvSeriesDetailsExtendedSummaryDataSource(
            params,
            tmdbService,
            movieDetailsMapper
        ).invoke()
    }
}