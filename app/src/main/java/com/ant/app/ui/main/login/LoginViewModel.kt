package com.ant.app.ui.main.login

import androidx.lifecycle.viewModelScope
import com.ant.app.ui.base.BaseViewModel
import com.ant.app.ui.extensions.parseResponse
import com.ant.common.logger.TmdbLogger
import com.ant.domain.usecases.login.LoginUserAndSaveSessionUseCase
import com.ant.domain.usecases.login.LogoutUserAndClearSessionUseCase
import com.ant.models.model.MoviesState
import com.ant.models.model.UserData
import com.ant.models.request.RequestType
import com.ant.models.session.SessionManager
import com.ant.models.source.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserAndSaveSessionUseCase,
    private val logoutUserUseCase: LogoutUserAndClearSessionUseCase,
    private val sessionManager: SessionManager,
    private val logger: TmdbLogger,
) : BaseViewModel<MoviesState<UserData>>(MoviesState()) {

    init {
        logger.d("LoginViewModel created.")
        viewModelScope.launch {
            sessionManager.getSessionId()?.let {
                logger.d("Session id: $it")
                state.value = MoviesState(
                    data = UserData(
                        username = sessionManager.getUsername(),
                        sessionId = it,
                    )
                )
            }
        }
    }

    fun login(
        username: String,
        password: String,
    ) {
        job?.cancel()
        job = viewModelScope.launch {
            if (sessionManager.isUserLoggedInToTmdbApi()) {
                logger.d("User is already logged in.")
                return@launch
            }

            loginUserUseCase.invoke(
                Repository.Params(
                    RequestType.LoginSessionRequest.WithCredentials(
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
        job?.cancel()
        job = viewModelScope.launch {
            logger.d("Logout user.")
            if (!sessionManager.isUserLoggedInToTmdbApi()) {
                logger.d("User is already logged out.")
                return@launch
            }
            logoutUserUseCase.invoke(
                Repository.Params(
                    RequestType.LoginSessionRequest.Logout(
                        username = sessionManager.getUsername(),
                    )
                )
            ).collectAndSetState {
                parseResponse(
                    response = it
                )
            }
        }
    }

    fun signUp() {
        // signup user.
        logger.d("Sign up user.")
    }
}