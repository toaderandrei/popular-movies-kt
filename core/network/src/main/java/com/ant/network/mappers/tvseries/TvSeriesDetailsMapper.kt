package com.ant.network.mappers.tvseries

import com.ant.models.entities.MovieCast
import com.ant.models.entities.MovieCrew
import com.ant.models.entities.MovieVideo
import com.ant.models.entities.TvShowDetails
import com.ant.models.entities.VideoType
import com.ant.network.mappers.Mapper
import com.uwetrottmann.tmdb2.entities.TvShow
import javax.inject.Inject
import javax.inject.Singleton
import com.uwetrottmann.tmdb2.enumerations.VideoType as TmdbVideoType

@Singleton
class TvSeriesDetailsMapper @Inject constructor(
    private val tvSeriesMapper: TvSeriesDataMapper,
) : Mapper<TvShow, TvShowDetails> {
    override suspend fun map(from: TvShow): TvShowDetails {
        // todo fix date. it is now stored as a string.
        var tvSeries = tvSeriesMapper.map(from)
        tvSeries = tvSeries.copy(
            numberOfEpisodes = from.number_of_episodes,
            _firstAirDate = from.first_air_date,
            _lastAirDate = from.last_air_date,
            numberOfSeasons = from.number_of_seasons,
            status = from.status
        )

        val castList = mutableListOf<MovieCast>()
        from.credits?.cast?.forEach {
            val movieCast = MovieCast(
                movieId = tvSeries.id,
                creditId = from.credits?.id,
                cast_id = it.cast_id,
                name = it.character,
                order = it.order,
                profileImagePath = it.profile_path
            )
            castList.add(movieCast)
        }

        val crewList = mutableListOf<MovieCrew>()
        from.credits?.crew?.forEach {
            val movieCrew = MovieCrew(
                movieId = tvSeries.id,
                creditsId = from.credits?.id,
                name = it.department,
                job = it.job,
                profilePath = it.profile_path
            )
            crewList.add(movieCrew)
        }

        val videos = mutableListOf<MovieVideo>()
        from.videos?.results?.forEach {
            val movieVideo = MovieVideo(
                movieId = tvSeries.id,
                key = it.key,
                iso_639_1 = it.iso_639_1,
                iso_3166_1 = it.iso_3166_1,
                size = it.size,
                name = it.name,
                type = it.type?.toDomain(),
                site = it.site
            )
            videos.add(movieVideo)
        }
        return TvShowDetails(tvSeries, videos, castList, crewList)
    }
}

private fun TmdbVideoType.toDomain(): VideoType {
    return when (this) {
        TmdbVideoType.CLIP -> VideoType.CLIP
        TmdbVideoType.FEATURETTE -> VideoType.FEATURETTE
        TmdbVideoType.OPENING_CREDITS -> VideoType.OPENING_CREDITS
        TmdbVideoType.TEASER -> VideoType.TEASER
        TmdbVideoType.TRAILER -> VideoType.TRAILER
    }
}