package com.ant.models.request

sealed class RequestType {
    class MovieRequest(val movieType: MovieType) : RequestType()
    class MovieRequestDetails(
        val tmdbMovieId: Long,
        val appendToResponseItems: List<MovieAppendToResponseItem> = mutableListOf()
    ) : RequestType()

    class TvShowRequest(val tvSeriesType: TvShowType) : RequestType()
    class TvSeriesRequestDetails(
        val tmdbTvSeriesId: Long,
        val appendToResponseItems: List<TvSeriesAppendToResponseItem> = mutableListOf()
    ) : RequestType()
}