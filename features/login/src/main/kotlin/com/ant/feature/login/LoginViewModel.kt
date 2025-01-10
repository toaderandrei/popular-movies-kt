package com.ant.feature.login

import androidx.lifecycle.ViewModel
import com.ant.analytics.AnalyticsHelper
import com.ant.analytics.CrashlyticsHelper
import com.ant.common.logger.TmdbLogger
import com.ant.feature.login.state.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
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
//        job?.cancel()
//        job = viewModelScope.launch {
//            if (sessionManager.isUserLoggedInToTmdbApi()) {
//                // logger.d("User is already logged in.")
//                return@launch
//            }
//
//        }
    }

}