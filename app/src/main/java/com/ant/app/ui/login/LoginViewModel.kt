package com.ant.app.ui.login

import androidx.lifecycle.viewModelScope
import com.ant.app.ui.base.BaseViewModel
import com.ant.app.ui.extensions.parseResponse
import com.ant.common.logger.TmdbLogger
import com.ant.domain.usecases.login.AuthenticateUserUseCase
import com.ant.models.entities.LoginSession
import com.ant.models.model.Error
import com.ant.models.model.Loading
import com.ant.models.model.MoviesState
import com.ant.models.model.Success
import com.ant.models.request.RequestType
import com.ant.models.session.SessionManager
import com.ant.models.source.delegates.SessionManagerDelegate
import com.ant.models.source.repositories.Repository
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val authenticateUserUseCase: AuthenticateUserUseCase,
    sessionManagerDelegate: SessionManagerDelegate,
    private val logger: TmdbLogger,
) : BaseViewModel<MoviesState<LoginSession?>>(MoviesState()) {
    private val sessionManager: SessionManager by sessionManagerDelegate

    fun authenticateUser(userName: String, password: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (sessionManager.isUserLoggedIn()) {
                logger.d("User is already logged in.")
                return@launch
            }

            authenticateUserUseCase.invoke(
                Repository.Params(
                    RequestType.LoginSessionRequest(userName, password)
                )
            ).collectAndSetState {
                parseResponse(
                    response = it,
                    onError = { throwable ->
                        logger.e(throwable, "Error: ${throwable.message}")
                    })
            }
        }
    }

    fun logout() {
        logger.d("Logout user.")
        // logout user.
    }

    fun signUp() {
        // signup user.
        logger.d("Sign up user.")
    }
}