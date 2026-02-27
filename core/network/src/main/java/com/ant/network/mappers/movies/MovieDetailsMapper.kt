package com.ant.network.mappers.movies

import com.ant.models.entities.MovieCast
import com.ant.models.entities.MovieCrew
import com.ant.models.entities.MovieDetails
import com.ant.models.entities.MovieReview
import com.ant.models.entities.MovieVideo
import com.ant.models.entities.VideoType
import com.ant.network.mappers.Mapper
import com.uwetrottmann.tmdb2.entities.Movie
import javax.inject.Inject
import javax.inject.Singleton
import com.uwetrottmann.tmdb2.enumerations.VideoType as TmdbVideoType

@Singleton
class MovieDetailsMapper @Inject constructor(
    private val movieDataMapper: MovieDataMapper,
) : Mapper<Movie, MovieDetails> {
    override suspend fun map(from: Movie): MovieDetails {
        // todo fix date. it is now stored as a string.
        var movieData = movieDataMapper.map(from)
        movieData = movieData.copy(
            runtime = from.runtime?.toString(),
            originalLanguage = from.original_language,
        )
        val movieReviews = mutableListOf<MovieReview>()
        from.reviews?.results?.map {
            val movieReview = MovieReview(
                tmdbId = from.id,
                content = it.content,
                url = it.url,
                movieId = 0,
                name = it.author
            )
            movieReviews.add(movieReview)
        }

        val castList = mutableListOf<MovieCast>()
        from.credits?.cast?.forEach {
            val movieCast = MovieCast(
                movieId = movieData.id,
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
                movieId = movieData.id,
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
                movieId = movieData.id,
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
        return MovieDetails(movieData, videos, castList, movieReviews, crewList)
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