package com.ant.models.source.mappers.tvseries

import com.ant.models.entities.TvShow
import com.ant.models.source.mappers.Mapper
import com.uwetrottmann.tmdb2.entities.BaseTvShow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TvSeriesDataMapper @Inject constructor() :
    Mapper<BaseTvShow, TvShow> {
    override suspend fun map(from: BaseTvShow): TvShow {
        // todo fix date. it is now stored as a string.
        return TvShow(
            id = from.id!!.toLong(),
            name = from.name,
            originalTitle = from.original_name,
            voteCount = from.vote_count,
            overview = from.overview,
            backDropPath = from.backdrop_path,
            posterPath = from.poster_path,
            originalLanguage = from.original_language,
            voteAverage = from.vote_average,
            _genres_ids = from.genre_ids,
            _firstAirDate = from.first_air_date,
        )
    }
}