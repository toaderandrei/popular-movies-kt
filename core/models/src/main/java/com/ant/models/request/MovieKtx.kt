package com.ant.models.request

import com.ant.tmdb.extensions.TmdbAppendToResponse
import com.ant.tmdb.extensions.TmdbAppendToResponseItem

fun RequestType.MovieRequestDetails.toMovieId(): Int {
    return this.tmdbMovieId.toInt()
}

fun RequestType.MovieRequestDetails.toAppendRequest(): TmdbAppendToResponse {
    val items = mutableListOf<TmdbAppendToResponseItem>()

    this.appendToResponseItems.forEach {
        when (it) {
            MovieAppendToResponseItem.REVIEWS -> items.add(TmdbAppendToResponseItem.REVIEWS)
            MovieAppendToResponseItem.CREDITS -> items.add(TmdbAppendToResponseItem.CREDITS)
            MovieAppendToResponseItem.VIDEOS -> items.add(TmdbAppendToResponseItem.VIDEOS)
            MovieAppendToResponseItem.MOVIE_CREDITS -> items.add(TmdbAppendToResponseItem.MOVIE_CREDITS)
        }
    }
    return TmdbAppendToResponse(*items.toTypedArray())
}