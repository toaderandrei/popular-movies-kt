package com.ant.domain.usecases.login

import com.ant.common.qualifiers.IoDispatcher
import com.ant.domain.usecases.UseCase
import com.ant.models.model.UserData
import com.ant.models.request.RequestType
import com.ant.data.repositories.Repository
import com.ant.data.repositories.login.LogoutUserAndClearSessionsRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogoutUserAndClearSessionUseCase @Inject constructor(
    private val logoutRepository: LogoutUserAndClearSessionsRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<RequestType.LoginSessionRequest.Logout, UserData>(dispatcher) {
    override suspend fun execute(parameters: RequestType.LoginSessionRequest.Logout): UserData {
        logoutRepository.performRequest(parameters)
        return UserData(
            sessionId = null,
            username = parameters.username
        )
    }
}