package com.ant.domain.usecases.login

import com.ant.common.qualifiers.IoDispatcher
import com.ant.data.repositories.AuthRepository
import com.ant.domain.usecases.resultFlow
import com.ant.models.model.Result
import com.ant.models.model.UserData
import com.ant.models.request.RequestType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUserToTmDbUseCase @Inject constructor(
    private val repository: AuthRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) {
    operator fun invoke(parameters: RequestType.LoginSessionRequest.WithCredentials): Flow<Result<UserData>> {
        return resultFlow(dispatcher) {
            repository.login(parameters)
        }
    }
}
