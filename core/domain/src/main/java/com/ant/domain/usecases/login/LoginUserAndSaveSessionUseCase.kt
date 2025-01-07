package com.ant.domain.usecases.login

import com.ant.common.logger.TmdbLogger
import com.ant.common.qualifiers.IoDispatcher
import com.ant.domain.usecases.UseCase
import com.ant.models.model.Result
import com.ant.models.model.UserData
import com.ant.models.request.RequestType
import com.ant.models.session.SessionManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LoginUserAndSaveSessionUseCase @Inject constructor(
    private val logger: TmdbLogger,
    private val loginUserToTmDbUseCase: LoginUserToTmDbUseCase,
    private val sessionManager: SessionManager,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<RequestType.LoginSessionRequest.WithCredentials, UserData>(
    dispatcher
) {
    override suspend fun execute(parameters: RequestType.LoginSessionRequest.WithCredentials): UserData {
        return if (!sessionManager.getSessionId().isNullOrEmpty()) {
            UserData(
                username = parameters.username,
            )
        } else loginUserToTmDbUseCase.invoke(parameters)
            .filterNot { it is Result.Loading }
            .map { result ->
                if (result is Result.Success) {
                    val userData = result.data
                    logger.d("Login to TmDb APi successful. SessionId: ${result.data.sessionId}")
                    userData.apply {
                        sessionId?.let { sessionId ->
                            sessionManager.saveSessionId(sessionId = sessionId)
                            sessionManager.saveUsername(username = userData.username)
                        }
                    }
                } else {
                    throw (result as Result.Error).throwable
                }
            }.first()
    }
}