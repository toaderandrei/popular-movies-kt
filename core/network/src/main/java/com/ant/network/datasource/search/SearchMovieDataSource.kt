package com.ant.network.datasource.search

import com.ant.common.exceptions.bodyOrThrow
import com.ant.common.exceptions.withRetry
import com.ant.models.entities.MovieData
import com.ant.models.request.RequestType
import com.ant.network.mappers.movies.MoviesListMapper
import com.uwetrottmann.tmdb2.Tmdb
import retrofit2.awaitResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchMovieDataSource @Inject constructor(
    private val tmdb: Tmdb,
    private val moviesListMapper: MoviesListMapper,
) {
    suspend operator fun invoke(params: RequestType.SearchMovieRequest): List<MovieData> {
        val searchResultResponse = withRetry {
            tmdb.searchService().movie(
                params.query,
                params.page,
                null, // language
                null, // region
                false, // includeAdult
                null, // year
                null, // primaryReleaseYear
            )
        }.awaitResponse()

        val genreResponse = tmdb.genreService().movie(null).awaitResponse()

        val searchResults = searchResultResponse.bodyOrThrow()
        val genreResults = genreResponse.bodyOrThrow()

        return moviesListMapper.map(Pair(searchResults, genreResults))
    }
}
