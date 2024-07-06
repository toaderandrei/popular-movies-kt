package com.ant.models.source.mappers.tvseries

import com.ant.models.entities.TvShow
import com.ant.models.source.extensions.toGenreAsStringList
import com.ant.models.source.mappers.Mapper
import com.uwetrottmann.tmdb2.entities.Genre
import com.uwetrottmann.tmdb2.entities.GenreResults
import com.uwetrottmann.tmdb2.entities.TvShowResultsPage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TvSeriesMapper @Inject constructor(
    private val tvSeriesDataMapper: TvSeriesDataMapper
) : Mapper<Pair<TvShowResultsPage, GenreResults>, List<TvShow>> {
    override suspend fun map(from: Pair<TvShowResultsPage, GenreResults>): List<TvShow> {
        val results = from.first
        val genres = from.second.genres
        return results.results?.map {
            val movie = tvSeriesDataMapper.map(it)
            val filteredGenres = movie._genres_ids?.filterGenres(genres)
            movie.copy(_genres = filteredGenres?.toGenreAsStringList())
        } ?: emptyList()
    }
}

private fun List<Int>.filterGenres(genres: List<Genre>?): List<Genre> {
    return genres?.filter { genre: Genre -> this.contains(genre.id) } ?: emptyList()
}