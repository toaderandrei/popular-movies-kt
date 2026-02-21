package com.ant.models.request

sealed interface RequestType {

    // Movies
    data class MovieRequest(val movieType: MovieType, val page: Int = 1) : RequestType

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
    data class TvShowRequest(val tvSeriesType: TvShowType, val page: Int = 1) : RequestType

    data class TvSeriesRequestDetails(
        val tmdbTvSeriesId: Long,
        val appendToResponseItems: List<TvSeriesAppendToResponseItem> = mutableListOf()
    ) : RequestType

    // Search
    data class SearchMovieRequest(val query: String, val page: Int = 1) : RequestType
    data class SearchTvShowRequest(val query: String, val page: Int = 1) : RequestType

    // Favorite
    data class FavoriteRequest(
        val sessionId: String,
        val favorite: Boolean,
        val favoriteId: Int,
        val mediaType: FavoriteType,
    ) : RequestType
}