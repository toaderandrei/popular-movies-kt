package com.ant.models.source.datasource.movies

import com.ant.models.request.RequestType
import com.ant.models.request.MovieType
import com.ant.models.entities.MovieData
import com.ant.models.source.extensions.bodyOrThrow
import com.ant.models.source.extensions.withRetry
import com.ant.models.source.mappers.movies.MoviesListMapper
import com.ant.models.source.repositories.Repository
import com.uwetrottmann.tmdb2.Tmdb
import com.uwetrottmann.tmdb2.entities.Genre
import com.uwetrottmann.tmdb2.entities.MovieResultsPage
import com.uwetrottmann.tmdb2.services.MoviesService
import retrofit2.Call
import retrofit2.awaitResponse

class MovieListDataSource(
    private val params: Repository.Params<RequestType.MovieRequest>,
    private val tmdb: Tmdb,
    private val moviesListMapper: MoviesListMapper
) {
    suspend operator fun invoke(): List<MovieData> {
        val movieService = tmdb.moviesService()

        val movieResultsPageResponse = when (params.request.movieType) {
            MovieType.POPULAR -> popular(movieService, params)
            MovieType.TOP_RATED -> topRated(movieService, params)
            MovieType.NOW_PLAYING -> nowPlaying(movieService, params)
            MovieType.UPCOMING -> upComing(movieService, params)
        }.awaitResponse()

        val genreResponse = tmdb.genreService().movie(null)
            .awaitResponse()

        val genresList = genreResponse.bodyOrThrow().genres
        val movieResultsPage = movieResultsPageResponse.bodyOrThrow()

        movieResultsPage.results?.map { movie ->
            val genres = mutableListOf<Genre>()
            movie.genre_ids?.forEach { genreId ->
                val filteredList = genresList?.first { it.id == genreId }
                filteredList?.let { genre ->
                    genres.add(genre)
                }
            }
            movie.genres = genres
        }

        return movieResultsPageResponse.let {
            moviesListMapper.map(
                movieResultsPage
            )
        }
    }

    private suspend fun topRated(
        moviesService: MoviesService,
        params: Repository.Params<RequestType.MovieRequest>,
    ): Call<MovieResultsPage> {
        return withRetry {
            moviesService.topRated(params.page, null, null)
        }
    }

    private suspend fun popular(
        moviesService: MoviesService,
        params: Repository.Params<RequestType>
    ): Call<MovieResultsPage> {
        return withRetry {
            moviesService.popular(params.page, null, null)
        }
    }

    private suspend fun nowPlaying(
        moviesService: MoviesService,
        params: Repository.Params<RequestType>,
    ): Call<MovieResultsPage> {
        return withRetry {
            moviesService.nowPlaying(params.page, null, null)
        }
    }

    private suspend fun upComing(
        moviesService: MoviesService,
        params: Repository.Params<RequestType>,
    ): Call<MovieResultsPage> {
        return withRetry {
            moviesService.upcoming(params.page, null, null)
        }
    }
}