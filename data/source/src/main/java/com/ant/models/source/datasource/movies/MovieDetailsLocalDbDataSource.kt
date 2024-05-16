package com.ant.models.source.datasource.movies

import com.ant.models.entities.MovieCast
import com.ant.models.entities.MovieData
import com.ant.models.entities.MovieDetails
import com.ant.models.entities.MovieVideo
import com.ant.models.source.database.MoviesDb

class MovieDetailsLocalDbDataSource(
    private val movieData: MovieData,
    private val moviesDb: MoviesDb,
) {
    suspend operator fun invoke(): MovieDetails {
        val movieVideos: List<MovieVideo> = moviesDb.movieVideosDao().loadVideosForMovieId(movieData.id)
        val movieCasts: List<MovieCast> = moviesDb.movieCastDao().loadMovieCastsForMovieId(movieData.id)

        return MovieDetails(
            movieData = movieData,
            videos = movieVideos,
            movieCasts = movieCasts,
            movieCrewList = null,
            reviews = null
        )
    }
}