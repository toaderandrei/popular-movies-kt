package com.ant.data.repositories.login

import com.ant.common.exceptions.bodyOrThrow
import com.ant.models.model.UserData
import com.ant.models.request.RequestType
import com.ant.network.mappers.login.LoginSessionMapper
import com.uwetrottmann.tmdb2.Tmdb
import retrofit2.awaitResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class LoginUserTmDbRepository @Inject constructor(
    private val tmDbApi: Tmdb,
    private val loginSessionMapper: LoginSessionMapper,
) {
    suspend fun performRequest(params: RequestType.LoginSessionRequest.WithCredentials): UserData {
        val authenticationService = tmDbApi.authenticationService()

        // First we need to request a token.
        val tokenResponseBody = authenticationService
            .requestToken()
            .awaitResponse()
            .bodyOrThrow()

        // Next we need to validate the token, username and password.
        val validate = tmDbApi.authenticationService()
            .validateToken(
                params.username,
                params.password,
                tokenResponseBody.request_token,
            )
            .awaitResponse()
            .bodyOrThrow()

        // If successful we fetch the session.
        val accountSession = tmDbApi
            .authenticationService()
            .createSession(
                validate.request_token,
            )
            .awaitResponse()
            .bodyOrThrow()

        // If successful we return the session.
        val session = loginSessionMapper.map(accountSession)
        return session.copy(username = params.username)
    }
}