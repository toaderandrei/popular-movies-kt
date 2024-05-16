package com.ant.models.source.mappers.movies

import com.ant.models.entities.MovieData
import com.ant.models.source.mappers.Mapper
import com.uwetrottmann.tmdb2.entities.MovieResultsPage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesListMapper @Inject constructor(
    private val movieDataMapper: MovieDataMapper,
) : Mapper<MovieResultsPage, List<MovieData>> {
    override suspend fun map(from: MovieResultsPage): List<MovieData> {
        return from.results?.map {
            movieDataMapper.map(it)
        } ?: emptyList()
    }
}