package com.ant.app.ui.main.profile

import androidx.lifecycle.viewModelScope
import com.ant.app.ui.base.BaseViewModel
import com.ant.app.ui.extensions.parseResponse
import com.ant.common.logger.TmdbLogger
import com.ant.domain.usecases.login.LoadAccountProfileUseCase
import com.ant.models.model.MoviesState
import com.ant.models.model.UserData
import com.ant.models.request.RequestType
import com.ant.models.source.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountProfileViewModel @Inject constructor(
    private val loadUserAccountUseCase: LoadAccountProfileUseCase,
    private val logger: TmdbLogger,
) : BaseViewModel<MoviesState<UserData>>(MoviesState()) {


    fun verifyIfUserIsLoggedIn() {
        viewModelScope.launch {
            loadUserAccountUseCase.invoke(Repository.Params(RequestType.LoginSessionRequest.GetUser))
                .collectAndSetState {
                    parseResponse(it)
                }
        }
    }
}