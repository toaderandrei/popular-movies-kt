package com.ant.network.mappers.tvseries

import com.ant.models.entities.TvShow
import com.ant.models.model.PaginatedResult
import com.ant.network.mappers.Mapper
import com.uwetrottmann.tmdb2.entities.Genre
import com.uwetrottmann.tmdb2.entities.GenreResults
import com.uwetrottmann.tmdb2.entities.TvShowResultsPage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TvSeriesMapper @Inject constructor(
    private val tvSeriesDataMapper: TvSeriesDataMapper
) : Mapper<Pair<TvShowResultsPage, GenreResults>, PaginatedResult<TvShow>> {
    override suspend fun map(from: Pair<TvShowResultsPage, GenreResults>): PaginatedResult<TvShow> {
        val results = from.first
        val genres = from.second.genres
        val items = results.results?.map {
            val movie = tvSeriesDataMapper.map(it)
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
    return genres?.filter { genre: Genre -> genre.id?.let { this.contains(it) } == true }
        ?: emptyList()
}