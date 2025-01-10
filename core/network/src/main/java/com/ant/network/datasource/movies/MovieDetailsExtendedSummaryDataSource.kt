package com.ant.network.datasource.movies


import com.ant.common.exceptions.bodyOrThrow
import com.ant.common.exceptions.withRetry
import com.ant.models.entities.MovieDetails
import com.ant.models.request.RequestType
import com.ant.models.request.toAppendRequest
import com.ant.models.request.toMovieId
import com.ant.network.mappers.movies.MovieDetailsMapper
import com.uwetrottmann.tmdb2.Tmdb
import retrofit2.awaitResponse

class MovieDetailsExtendedSummaryDataSource(
    private val params: RequestType.MovieRequestDetails,
    private val tmdb: Tmdb,
    private val movieDetailsMapper: MovieDetailsMapper
) {
    suspend operator fun invoke(): MovieDetails {
        val movieService = tmdb.moviesService()
        return withRetry {
            movieService.summary(params.toMovieId(), null, params.toAppendRequest())
        }.awaitResponse().let {
            movieDetailsMapper.map(
                it.bodyOrThrow()
            )
        }
    }
}