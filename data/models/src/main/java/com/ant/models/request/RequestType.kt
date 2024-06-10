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

    data class LoginSessionRequest(
        val username: String? = null,
        val password: String
    ) : RequestType

    sealed interface FirebaseRequest : RequestType {
        data class SignIn(val email: String?, val password: String?) : FirebaseRequest
        data class SignUp(val username: String?, val password: String?) : FirebaseRequest

        object SignOut : FirebaseRequest

        object GetUser : FirebaseRequest

        data class SendPasswordReset(val email: String) : FirebaseRequest
    }
}