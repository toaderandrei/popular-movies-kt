package com.ant.models.request

fun RequestType.TvSeriesRequestDetails.toMovieId(): Int {
    return this.tmdbTvSeriesId.toInt()
}

fun RequestType.TvSeriesRequestDetails.toAppendRequest(): TmdbAppendToResponse {
    val items = mutableListOf<TmdbAppendToResponseItem>()

    this.appendToResponseItems.forEach {
        when (it) {
            TvSeriesAppendToResponseItem.REVIEWS -> items.add(TmdbAppendToResponseItem.REVIEWS)
            TvSeriesAppendToResponseItem.CREDITS -> items.add(TmdbAppendToResponseItem.CREDITS)
            TvSeriesAppendToResponseItem.VIDEOS -> items.add(TmdbAppendToResponseItem.VIDEOS)
        }
    }
    return TmdbAppendToResponse(*items.toTypedArray())
}