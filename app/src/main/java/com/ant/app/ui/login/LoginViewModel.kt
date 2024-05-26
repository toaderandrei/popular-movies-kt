package com.ant.app.ui.login

import androidx.lifecycle.viewModelScope
import com.ant.app.ui.base.BaseViewModel
import com.ant.app.ui.extensions.parseResponse
import com.ant.common.logger.TmdbLogger
import com.ant.domain.usecases.login.AuthenticateUserToTmDbAndSaveSessionUseCase
import com.ant.models.entities.LoginSession
import com.ant.models.model.MoviesState
import com.ant.models.request.RequestType
import com.ant.models.session.SessionManager
import com.ant.models.source.delegates.SessionManagerDelegate
import com.ant.models.source.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authenticateUserUseCase: AuthenticateUserToTmDbAndSaveSessionUseCase,
    sessionManagerDelegate: SessionManagerDelegate,
    private val logger: TmdbLogger,
) : BaseViewModel<MoviesState<LoginSession>>(MoviesState()) {
    private val sessionManager: SessionManager by sessionManagerDelegate

    init {
        logger.d("LoginViewModel created.")
        viewModelScope.launch {
            val username = sessionManager.getUsername()
            sessionManager.getSessionId()?.let {
                logger.d("Session id: $it")
                state.value = MoviesState(data = LoginSession(sessionId = it, username = username))
            }
        }

    }

    fun authenticateUser(
        username: String,
        password: String,
    ) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (sessionManager.isUserLoggedInToTmdbApi()) {
                logger.d("User is already logged in.")
                return@launch
            }

            authenticateUserUseCase.invoke(
                Repository.Params(
                    RequestType.LoginSessionRequest(
                        username = username, password = password
                    )
                )
            ).collectAndSetState {
                parseResponse(
                    response = it,
                )
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