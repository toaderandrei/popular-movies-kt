package com.ant.models.request

sealed interface RequestType {
    data class MovieRequest(val movieType: MovieType) : RequestType

    data class MovieRequestDetails(
        val tmdbMovieId: Long,
        val appendToResponseItems: List<MovieAppendToResponseItem> = mutableListOf()
    ) : RequestType

    data class TvShowRequest(val tvSeriesType: TvShowType) : RequestType

    data class TvSeriesRequestDetails(
        val tmdbTvSeriesId: Long,
        val appendToResponseItems: List<TvSeriesAppendToResponseItem> = mutableListOf()
    ) : RequestType

    sealed interface LoginSessionRequest : RequestType {
        data class WithCredentials(
            val username: String,
            val password: String,
        ) : LoginSessionRequest

        data class Logout(
            val username: String?,
        ) : LoginSessionRequest

        data object GetUser : LoginSessionRequest
    }
}