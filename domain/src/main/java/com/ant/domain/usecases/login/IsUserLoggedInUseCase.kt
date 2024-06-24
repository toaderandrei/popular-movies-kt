package com.ant.domain.usecases.login

import com.ant.domain.qualifiers.IoDispatcher
import com.ant.domain.usecases.UseCase
import com.ant.models.request.RequestType
import com.ant.models.session.SessionManager
import com.ant.models.source.repositories.Repository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IsUserLoggedInUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    private val sessionManager: SessionManager,
) : UseCase<Repository.Params<RequestType.FirebaseRequest.GetUser>, Boolean>(dispatcher) {
    override suspend fun execute(parameters: Repository.Params<RequestType.FirebaseRequest.GetUser>): Boolean {
        return sessionManager.getSessionId() != null
    }

}