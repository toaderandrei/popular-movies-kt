package com.ant.models.request

sealed interface RequestType {

    // Movies
    data class MovieRequest(val movieType: MovieType) : RequestType

    data class MovieRequestDetails(
        val tmdbMovieId: Long,
        val appendToResponseItems: List<MovieAppendToResponseItem> = mutableListOf()
    ) : RequestType

    // Login
    sealed interface LoginSessionRequest : RequestType {
        data class WithCredentials(
            val username: String,
            val password: String? = null,
        ) : LoginSessionRequest

        data class Logout(
            val username: String?,
        ) : LoginSessionRequest

        data object GetUser : LoginSessionRequest
    }

    // TV Show
    data class TvShowRequest(val tvSeriesType: TvShowType) : RequestType

    data class TvSeriesRequestDetails(
        val tmdbTvSeriesId: Long,
        val appendToResponseItems: List<TvSeriesAppendToResponseItem> = mutableListOf()
    ) : RequestType

    // Favorite
    data class FavoriteRequest(val sessionId: String, val favorite: Boolean, val favoriteId: Int) :
        RequestType
}