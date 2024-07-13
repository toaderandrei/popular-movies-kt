package com.ant.domain.usecases.login

import com.ant.common.qualifiers.IoDispatcher
import com.ant.domain.usecases.UseCase
import com.ant.models.model.UserData
import com.ant.models.request.RequestType
import com.ant.models.source.repositories.Repository
import com.ant.models.source.repositories.login.LoginUserTmDbRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginUserToTmDbUseCase @Inject constructor(
    private val repository: LoginUserTmDbRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Repository.Params<RequestType.LoginSessionRequest.WithCredentials>, UserData>(dispatcher) {
    override suspend fun execute(parameters: Repository.Params<RequestType.LoginSessionRequest.WithCredentials>): UserData {
        return repository.performRequest(parameters)
    }
}