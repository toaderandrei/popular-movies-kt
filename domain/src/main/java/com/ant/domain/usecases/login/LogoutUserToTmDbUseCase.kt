package com.ant.domain.usecases.login

import com.ant.domain.qualifiers.IoDispatcher
import com.ant.domain.usecases.UseCase
import com.ant.models.model.UserData
import com.ant.models.request.RequestType
import com.ant.models.source.repositories.Repository
import com.ant.models.source.repositories.login.LogoutUserAndClearSessionsRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogoutUserToTmDbUseCase @Inject constructor(
    private val logoutRepository: LogoutUserAndClearSessionsRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Repository.Params<RequestType.LoginSessionRequest.Logout>, UserData>(dispatcher) {
    override suspend fun execute(parameters: Repository.Params<RequestType.LoginSessionRequest.Logout>): UserData {
        logoutRepository.performRequest(parameters)
        return UserData(
            sessionId = null,
            username = parameters.request.username
        )
    }
}