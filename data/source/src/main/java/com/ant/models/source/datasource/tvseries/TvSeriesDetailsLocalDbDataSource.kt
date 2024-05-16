package com.ant.models.source.datasource.tvseries

import com.ant.models.entities.MovieCast
import com.ant.models.entities.MovieVideo
import com.ant.models.entities.TvShow
import com.ant.models.entities.TvShowDetails
import com.ant.models.source.database.MoviesDb

class TvSeriesDetailsLocalDbDataSource(
    private val movieData: TvShow,
    private val moviesDb: MoviesDb,
) {
    suspend operator fun invoke(): TvShowDetails {
        val movieVideos: List<MovieVideo> = moviesDb.movieVideosDao().loadVideosForMovieId(movieData.id)
        val movieCasts: List<MovieCast> = moviesDb.movieCastDao().loadMovieCastsForMovieId(movieData.id)

        return TvShowDetails(
            tvSeriesData = movieData,
            videos = movieVideos,
            tvSeriesCasts = movieCasts,
            movieCrewList = null,
        )
    }
}