package com.ant.feature.login.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ant.analytics.AnalyticsEvent
import com.ant.analytics.AnalyticsHelper
import com.ant.analytics.CrashlyticsHelper
import com.ant.domain.usecases.login.LoadAccountProfileUseCase
import com.ant.domain.usecases.login.LogoutUserAndClearSessionUseCase
import com.ant.models.model.Result
import com.ant.models.request.RequestType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val loadAccountProfileUseCase: LoadAccountProfileUseCase,
    private val logoutUseCase: LogoutUserAndClearSessionUseCase,
    private val analyticsHelper: AnalyticsHelper,
    private val crashlyticsHelper: CrashlyticsHelper,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            loadAccountProfileUseCase(Unit).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Result.Success -> {
                        val userData = result.data
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isLoggedIn = userData.sessionId != null,
                                username = userData.username,
                                sessionId = userData.sessionId,
                            )
                        }
                    }
                    is Result.Error -> {
                        crashlyticsHelper.logError(result.throwable)
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isLoggedIn = false,
                            )
                        }
                    }
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            val username = _uiState.value.username
            logoutUseCase(
                RequestType.LoginSessionRequest.Logout(username = username)
            ).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoggingOut = true) }
                    }
                    is Result.Success -> {
                        analyticsHelper.logEvent(
                            AnalyticsEvent(
                                type = AnalyticsEvent.Types.ACCOUNT_PROFILE,
                                extras = listOf(
                                    AnalyticsEvent.Param(
                                        AnalyticsEvent.ParamKeys.LOGOUT_USER.name,
                                        username,
                                    )
                                )
                            )
                        )
                        _uiState.update {
                            it.copy(
                                isLoggingOut = false,
                                logoutSuccess = true,
                                isLoggedIn = false,
                                username = null,
                                sessionId = null,
                            )
                        }
                    }
                    is Result.Error -> {
                        crashlyticsHelper.logError(result.throwable)
                        _uiState.update {
                            it.copy(
                                isLoggingOut = false,
                                error = result.throwable.message ?: "Logout failed",
                            )
                        }
                    }
                }
            }
        }
    }
}
