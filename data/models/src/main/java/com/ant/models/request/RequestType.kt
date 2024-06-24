package com.ant.models.request

import com.ant.models.session.SessionManager

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
        ): LoginSessionRequest

        data class WithSessionId(val sessionId: String) : LoginSessionRequest
    }

    sealed interface FirebaseRequest : RequestType {
        data class SignIn(val email: String?, val password: String?) : FirebaseRequest
        data class SignUp(val username: String?, val password: String?) : FirebaseRequest

        object SignOut : FirebaseRequest

        object GetUser : FirebaseRequest

        data class SendPasswordReset(val email: String) : FirebaseRequest
    }
}