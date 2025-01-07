package com.ant.app.ui.main.login

import androidx.lifecycle.viewModelScope
import com.ant.analytics.AnalyticsEvent
import com.ant.analytics.AnalyticsHelper
import com.ant.analytics.CrashlyticsHelper
import com.ant.ui.viewmodels.BaseViewModel
import com.ant.app.ui.extensions.parseResponse
import com.ant.common.logger.TmdbLogger
import com.ant.domain.usecases.login.LoginUserAndSaveSessionUseCase
import com.ant.domain.usecases.login.LogoutUserAndClearSessionUseCase
import com.ant.models.model.UIState
import com.ant.models.model.UserData
import com.ant.models.request.RequestType
import com.ant.models.session.SessionManager
import com.ant.models.source.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModelOld @Inject constructor(
    private val loginUserUseCase: LoginUserAndSaveSessionUseCase,
    private val logoutUserUseCase: LogoutUserAndClearSessionUseCase,
    private val sessionManager: SessionManager,
    private val analyticsHelper: AnalyticsHelper,
    private val crashlyticsHelper: CrashlyticsHelper,
    private val logger: TmdbLogger,
) : BaseViewModel<UIState<UserData>>(UIState()) {

    init {
        logger.d("LoginViewModel created.")
        viewModelScope.launch {
            sessionManager.getUsername()?.let { username ->
                logger.d("Username: $username")
                loginUserUseCase.invoke(
                    RequestType.LoginSessionRequest.WithCredentials(
                        username,
                        null
                    )
                ).collectAndSetState { response ->
                    parseResponse(response)
                }
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
                RequestType.LoginSessionRequest.WithCredentials(
                    username = username, password = password
                )
            ).collectAndSetState {
                parseResponse(
                    response = it,
                    onSuccess = { success ->
                        analyticsHelper.logEvent(
                            AnalyticsEvent(
                                type = AnalyticsEvent.Types.LOGIN_SCREEN,
                                mutableListOf(
                                    AnalyticsEvent.Param(
                                        AnalyticsEvent.ParamKeys.LOGIN_NAME.name,
                                        success?.username
                                    )
                                )
                            )
                        )
                    },
                    onError = { error ->
                        logger.e(error, "Logging to crashlytics: $error")
                        crashlyticsHelper.logError(throwable = error)
                    }
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
                    response = it,
                )
            }
        }
    }

    fun signUp() {
        // signup user.
        logger.d("Sign up user.")
    }
}