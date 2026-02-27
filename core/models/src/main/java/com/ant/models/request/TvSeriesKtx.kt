package com.ant.models.request

import com.ant.tmdb.extensions.TmdbAppendToResponse

fun RequestType.TvSeriesRequestDetails.toMovieId(): Int {
    return this.tmdbTvSeriesId.toInt()
}

fun RequestType.TvSeriesRequestDetails.toAppendRequest(): com.ant.tmdb.extensions.TmdbAppendToResponse {
    val items = mutableListOf<com.ant.tmdb.extensions.TmdbAppendToResponseItem>()

    this.appendToResponseItems.forEach {
        when (it) {
            TvSeriesAppendToResponseItem.REVIEWS -> items.add(com.ant.tmdb.extensions.TmdbAppendToResponseItem.REVIEWS)
            TvSeriesAppendToResponseItem.CREDITS -> items.add(com.ant.tmdb.extensions.TmdbAppendToResponseItem.CREDITS)
            TvSeriesAppendToResponseItem.VIDEOS -> items.add(com.ant.tmdb.extensions.TmdbAppendToResponseItem.VIDEOS)
        }
    }
    return TmdbAppendToResponse(*items.toTypedArray())
}