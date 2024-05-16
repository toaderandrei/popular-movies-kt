package com.ant.models.source.datasource.movies


import com.ant.models.entities.MovieDetails
import com.ant.models.request.RequestType
import com.ant.models.request.toAppendRequest
import com.ant.models.request.toMovieId
import com.ant.models.source.extensions.bodyOrThrow
import com.ant.models.source.extensions.withRetry
import com.ant.models.source.mappers.movies.MovieDetailsMapper
import com.ant.models.source.repositories.Repository
import com.uwetrottmann.tmdb2.Tmdb
import retrofit2.awaitResponse

class MovieDetailsExtendedSummaryDataSource(
    private val params: Repository.Params<RequestType.MovieRequestDetails>,
    private val tmdb: Tmdb,
    private val movieDetailsMapper: MovieDetailsMapper
) {
    suspend operator fun invoke(): MovieDetails {
        val movieService = tmdb.moviesService()
        return withRetry {
            movieService.summary(params.request.toMovieId(), null, params.request.toAppendRequest())
        }.awaitResponse().let {
            movieDetailsMapper.map(
                it.bodyOrThrow()
            )
        }
    }
}