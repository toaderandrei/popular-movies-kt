package com.ant.models.source.mappers.movies

import com.ant.models.entities.MovieData
import com.ant.models.source.extensions.toGenreAsStringList
import com.ant.models.source.mappers.Mapper
import com.uwetrottmann.tmdb2.entities.BaseMovie
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MovieDataMapper @Inject constructor() : Mapper<BaseMovie, MovieData> {
    override suspend fun map(from: BaseMovie): MovieData {
        // todo fix date. it is now stored as a string.
        return MovieData(
            id = from.id!!.toLong(),
            title = from.title,
            originalTitle = from.original_title,
            voteCount = from.vote_count,
            overview = from.overview,
            backDropPath = from.backdrop_path,
            posterPath = from.poster_path,
            voteAverage = from.vote_average,
            _releaseDate = from.release_date,
            _genres_ids = from.genre_ids,
            _genres = from.genres?.toGenreAsStringList()
        )
    }
}