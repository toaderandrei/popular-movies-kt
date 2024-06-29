package com.ant.models.source.repositories.login

import com.ant.models.request.RequestType
import com.ant.models.session.SessionManager
import com.ant.models.source.repositories.Repository
import com.uwetrottmann.tmdb2.Tmdb
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogoutUserAndClearSessionsRepository @Inject constructor(
    private val tmdbApi: Tmdb,
    private val sessionManager: SessionManager,
) : Repository<Repository.Params<RequestType.LoginSessionRequest.Logout>, Boolean> {
    override suspend fun performRequest(params: Repository.Params<RequestType.LoginSessionRequest.Logout>): Boolean {
        tmdbApi.clearSessions()
        sessionManager.clearSessionAndSignOut()
        return true
    }
}