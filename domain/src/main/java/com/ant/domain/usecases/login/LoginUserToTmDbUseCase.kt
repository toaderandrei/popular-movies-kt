package com.ant.domain.usecases.login

import com.ant.domain.qualifiers.IoDispatcher
import com.ant.domain.usecases.UseCase
import com.ant.models.entities.LoginSession
import com.ant.models.request.RequestType
import com.ant.models.source.repositories.Repository
import com.ant.models.source.repositories.login.AuthenticateTmDbRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginUserToTmDbUseCase @Inject constructor(
    private val repository: AuthenticateTmDbRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Repository.Params<RequestType.LoginSessionRequest.WithCredentials>, LoginSession>(dispatcher) {
    override suspend fun execute(parameters: Repository.Params<RequestType.LoginSessionRequest.WithCredentials>): LoginSession {
        return repository.fetchData(parameters)
    }
}