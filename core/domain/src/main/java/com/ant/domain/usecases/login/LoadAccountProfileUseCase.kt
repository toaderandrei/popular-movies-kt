package com.ant.domain.usecases.login

import com.ant.common.qualifiers.IoDispatcher
import com.ant.domain.usecases.resultFlow
import com.ant.models.model.Result
import com.ant.models.model.UserData
import com.ant.models.session.SessionManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadAccountProfileUseCase @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val sessionManager: SessionManager,
) {
    operator fun invoke(parameters: Unit): Flow<Result<UserData>> {
        return resultFlow(dispatcher) {
            val sessionId = sessionManager.getSessionId()
            val username = sessionManager.getUsername()
            UserData(
                username = username,
                sessionId = sessionId,
            )
        }
    }
}
