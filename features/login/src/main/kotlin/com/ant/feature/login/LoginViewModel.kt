package com.ant.feature.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ant.analytics.AnalyticsEvent
import com.ant.analytics.AnalyticsHelper
import com.ant.analytics.CrashlyticsHelper
import com.ant.common.logger.TmdbLogger
import com.ant.domain.usecases.login.LoginUserAndSaveSessionUseCase
import com.ant.feature.login.state.LoginState
import com.ant.models.model.fold
import com.ant.models.request.RequestType
import com.ant.models.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val loginUserUseCase: LoginUserAndSaveSessionUseCase,
    private val analyticsHelper: AnalyticsHelper,
    private val crashlyticsHelper: CrashlyticsHelper,
    private val logger: TmdbLogger,
) : ViewModel() {

    private var job: Job? = null
    private val _stateAsFlow: MutableStateFlow<LoginState> =
        MutableStateFlow(LoginState.Idle)
    val stateAsFlow: StateFlow<LoginState> get() = _stateAsFlow

    fun login(
        username: String,
        password: String,
    ) {
        job?.cancel()
        job = viewModelScope.launch {
            if (sessionManager.isUserLoggedInToTmdbApi()) {
                // logger.d("User is already logged in.")
                return@launch
            }

            loginUserUseCase.invoke(
                RequestType.LoginSessionRequest.WithCredentials(
                    username = username, password = password
                )
            ).collect {
                it.fold(
                    onSuccess = {
                        _stateAsFlow.value = LoginState.Success
                        analyticsHelper.logEvent(
                            AnalyticsEvent(
                                type = AnalyticsEvent.Types.LOGIN_SCREEN,
                                mutableListOf(
                                    AnalyticsEvent.Param(
                                        AnalyticsEvent.ParamKeys.LOGIN_NAME.name,
                                        username
                                    )
                                )
                            )
                        )
                    },
                    onFailure = { error ->
                        _stateAsFlow.value = LoginState.Error(error)
                        logger.e(error, "Logging to crashlytics: $error")
                        crashlyticsHelper.logError(throwable = error)
                    },
                    onLoading = {
                        _stateAsFlow.value = LoginState.Loading
                    }
                )
            }
        }
    }

}