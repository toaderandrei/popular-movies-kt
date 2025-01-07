package com.ant.app.ui.main.profile

import androidx.lifecycle.viewModelScope
import com.ant.analytics.AnalyticsEvent
import com.ant.analytics.AnalyticsHelper
import com.ant.analytics.CrashlyticsHelper
import com.ant.ui.viewmodels.BaseViewModel
import com.ant.app.ui.extensions.parseResponse
import com.ant.common.logger.TmdbLogger
import com.ant.domain.usecases.login.LoadAccountProfileUseCase
import com.ant.domain.usecases.login.LogoutUserAndClearSessionUseCase
import com.ant.models.model.UIState
import com.ant.models.model.UserData
import com.ant.models.request.RequestType
import com.ant.models.source.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountProfileViewModel @Inject constructor(
    private val loadUserAccountUseCase: LoadAccountProfileUseCase,
    private val logoutUserAndClearSessionUseCase: LogoutUserAndClearSessionUseCase,
    private val analyticsHelper: AnalyticsHelper,
    private val crashlyticsHelper: CrashlyticsHelper,
    private val logger: TmdbLogger,
) : BaseViewModel<UIState<UserData>>(UIState()) {

    fun verifyIfUserIsLoggedIn() {
        viewModelScope.launch {
            loadUserAccountUseCase.invoke(Unit)
                .collectAndSetState {
                    parseResponse(it)
                }
        }
    }

    fun logout(username: String?) {
        viewModelScope.launch {
            logoutUserAndClearSessionUseCase.invoke(
                Repository.Params(
                    RequestType.LoginSessionRequest.Logout(
                        username = username
                    )
                )
            ).collectAndSetState {
                parseResponse(it, onSuccess = { success ->
                    analyticsHelper.logEvent(
                        AnalyticsEvent(
                            type = AnalyticsEvent.Types.ACCOUNT_PROFILE, mutableListOf(
                                AnalyticsEvent.Param(
                                    AnalyticsEvent.ParamKeys.LOGOUT_USER.name, success?.username
                                )
                            )
                        )
                    )
                }, onError = { error ->
                    crashlyticsHelper.logError(error)
                })
            }
        }
    }
}