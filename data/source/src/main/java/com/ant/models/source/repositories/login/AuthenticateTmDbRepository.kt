package com.ant.models.source.repositories.login

import com.ant.models.entities.LoginSession
import com.ant.models.request.RequestType
import com.ant.models.source.extensions.bodyOrThrow
import com.ant.models.source.mappers.login.LoginSessionMapper
import com.ant.models.source.repositories.Repository
import com.uwetrottmann.tmdb2.Tmdb
import retrofit2.awaitResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticateTmDbRepository @Inject constructor(
    private val tmDbApi: Tmdb,
    private val loginSessionMapper: LoginSessionMapper,
) : Repository<Repository.Params<RequestType.LoginSessionRequest>, LoginSession> {
    override suspend fun fetchData(params: Repository.Params<RequestType.LoginSessionRequest>): LoginSession {
        val authenticationService = tmDbApi.authenticationService()

        // First we need to request a token.
        val tokenResponseBody = authenticationService
            .requestToken()
            .awaitResponse()
            .bodyOrThrow()

        // Next we need to validate the token, username and password.
        val validate = tmDbApi.authenticationService()
            .validateToken(
                params.request.username,
                params.request.password,
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
        return loginSessionMapper.map(accountSession)
    }
}