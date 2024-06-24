package com.ant.domain.usecases.login

import com.ant.common.logger.TmdbLogger
import com.ant.domain.qualifiers.IoDispatcher
import com.ant.domain.usecases.UseCase
import com.ant.models.entities.LoginSession
import com.ant.models.model.Result
import com.ant.models.request.RequestType
import com.ant.models.session.SessionManager
import com.ant.models.source.repositories.Repository
import com.uwetrottmann.tmdb2.exceptions.TmdbException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class AuthenticateUserToTmDbAndSaveSessionUseCase @Inject constructor(
    private val logger: TmdbLogger,
    private val loginUserToTmDbUseCase: LoginUserToTmDbUseCase,
    private val sessionManager: SessionManager,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Repository.Params<RequestType.LoginSessionRequest.WithCredentials>, LoginSession>(dispatcher) {
    override suspend fun execute(parameters: Repository.Params<RequestType.LoginSessionRequest.WithCredentials>): LoginSession {
        val tmDbSession = loginUserToTmDbUseCase.invoke(parameters).firstOrNull {
            it is Result.Success || it is Result.Error
        }

        logger.d("Login to TmDb APi successful: $tmDbSession")

        if (tmDbSession is Result.Error || tmDbSession == null || tmDbSession.get() == null) {
            throw TmdbException("Error: ${tmDbSession?.get()}")
        }

        logger.d("Login to TmDb APi successful. SessionId: ${tmDbSession.get()?.sessionId}")

        tmDbSession.get()?.sessionId.let {
            // We retrieved session from tmdb api. Next authenticate user with firebase.
            sessionManager.saveSessionId(it)
            sessionManager.saveUsername(username = parameters.request.username)
        }
        return tmDbSession.get()!!
    }
}