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

class AuthenticateUserToTmdbAndSaveSessionUseCase @Inject constructor(
    private val logger: TmdbLogger,
    private val loginUserToTmDbUseCase: LoginUserToTmDbUseCase,
    private val sessionManager: SessionManager,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Repository.Params<RequestType.LoginSessionRequest>, LoginSession>(dispatcher) {
    override suspend fun execute(parameters: Repository.Params<RequestType.LoginSessionRequest>): LoginSession {
        val session = loginUserToTmDbUseCase.invoke(parameters).firstOrNull {
            it is Result.Success || it is Result.Error
        }

        logger.d("Login to TmDb APi successful: $session")

        if (session is Result.Error || session == null) {
            throw TmdbException("Error: ${session?.get()}")
        }

        logger.d("Login to TmDb APi successful. SessionId: ${session.get()?.sessionId}")

        return session.get()?.sessionId.let {
            // We retrieved session from tmdb api. Next authenticate user with firebase.
            sessionManager.saveSessionId(it)

            LoginSession(
                sessionId = it
            )
        }
    }
}