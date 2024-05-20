package com.ant.domain.usecases.login

import com.ant.common.logger.TmdbLogger
import com.ant.domain.qualifiers.IoDispatcher
import com.ant.domain.usecases.UseCase
import com.ant.models.entities.LoginSession
import com.ant.models.model.Result
import com.ant.models.model.getErrorOrNull
import com.ant.models.request.RequestType
import com.ant.models.session.SessionManager
import com.ant.models.source.repositories.Repository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class AuthenticateUserUseCase @Inject constructor(
    private val logger: TmdbLogger,
    private val loginUserToFirebaseUseCase: LoginUserToFirebaseUseCase,
    private val loginUserToTmDbUseCase: LoginUserToTmDbUseCase,
    private val sessionManager: SessionManager,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Repository.Params<RequestType.LoginSessionRequest>, LoginSession?>(dispatcher) {
    override suspend fun execute(parameters: Repository.Params<RequestType.LoginSessionRequest>): LoginSession? {
        val session = loginUserToTmDbUseCase.invoke(parameters).firstOrNull {
            it is Result.Success || it is Result.Error
        }

        if (session is Result.Error || session == null) {
            throw Exception("Error: ${session?.getErrorOrNull()}")
        }

        return session.get().let {
            // We retrieved session from tmdb api. Next authenticate user with firebase.
            //
            val firebaseUser = loginUserToFirebaseUseCase(
                Repository.Params(
                    RequestType.FirebaseRequest.SignIn(
                        parameters.request.username, parameters.request.password
                    )
                )
            ).firstOrNull { firebaseResult -> firebaseResult is Result.Success || firebaseResult is Result.Error }
            // If firebase user is not null, save session id to session manager.

            when (firebaseUser) {
                is Result.Success -> {
                    // Save user to session manager.
                    logger.d("Success: ${firebaseUser.data} and session id: ${it?.sessionId}")
                    sessionManager.saveSessionId(it?.sessionId)

                    LoginSession(
                        sessionId = it?.sessionId
                    )
                }

                else -> {
                    throw Exception("Error: ${session.getErrorOrNull()}")
                }
            }

        }
    }
}