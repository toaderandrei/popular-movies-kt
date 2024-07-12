package com.ant.domain.usecases.login

import com.ant.domain.qualifiers.IoDispatcher
import com.ant.domain.usecases.UseCase
import com.ant.models.model.UserData
import com.ant.models.request.RequestType
import com.ant.models.session.SessionManager
import com.ant.models.source.repositories.Repository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadAccountProfileUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    private val sessionManager: SessionManager,
) : UseCase<Repository.Params<RequestType.LoginSessionRequest.GetUser>, UserData>(dispatcher) {
    override suspend fun execute(parameters: Repository.Params<RequestType.LoginSessionRequest.GetUser>): UserData {
        val sessionId = sessionManager.getSessionId()
        val username = sessionManager.getUsername()
        return UserData(
            username = username,
            sessionId = sessionId,
        )
    }
}