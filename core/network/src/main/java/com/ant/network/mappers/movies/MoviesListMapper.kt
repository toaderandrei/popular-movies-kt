package com.ant.network.mappers.movies

import com.ant.models.entities.MovieData
import com.ant.models.model.PaginatedResult
import com.ant.network.mappers.Mapper
import com.uwetrottmann.tmdb2.entities.Genre
import com.uwetrottmann.tmdb2.entities.GenreResults
import com.uwetrottmann.tmdb2.entities.MovieResultsPage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesListMapper @Inject constructor(
    private val movieDataMapper: MovieDataMapper,
) : Mapper<Pair<MovieResultsPage, GenreResults>, PaginatedResult<MovieData>> {
    override suspend fun map(from: Pair<MovieResultsPage, GenreResults>): PaginatedResult<MovieData> {
        val results = from.first
        val genres = from.second.genres
        val items = results.results?.map {
            val movie = movieDataMapper.map(it)
            val filteredGenres = movie._genres_ids?.filterGenres(genres)
            movie.copy(_genres = emptyList()) //TODO: filteredGenres?.toGenreAsStringList()
        } ?: emptyList()
        return PaginatedResult(
            items = items,
            page = results.page ?: 1,
            totalPages = results.total_pages ?: 1,
        )
    }
}

private fun List<Int>.filterGenres(genres: List<Genre>?): List<Genre> {
    return genres?.filter { genre: Genre -> this.contains(genre.id) } ?: emptyList()
}