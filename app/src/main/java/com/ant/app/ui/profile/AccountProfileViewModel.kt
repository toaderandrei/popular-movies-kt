package com.ant.app.ui.profile

import androidx.lifecycle.viewModelScope
import com.ant.app.ui.base.BaseViewModel
import com.ant.app.ui.extensions.parseResponse
import com.ant.common.logger.TmdbLogger
import com.ant.domain.usecases.login.IsUserLoggedInUseCase
import com.ant.models.model.Error
import com.ant.models.model.Loading
import com.ant.models.model.MoviesState
import com.ant.models.model.Success
import com.ant.models.request.RequestType
import com.ant.models.source.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountProfileViewModel @Inject constructor(
    private val loginUseCase: IsUserLoggedInUseCase,
    private val logger: TmdbLogger,
) : BaseViewModel<MoviesState<Boolean>>(MoviesState()) {
    init {
        verifyIfUserIsLoggedIn()
    }

    private fun verifyIfUserIsLoggedIn() {
        viewModelScope.launch {
            loginUseCase.invoke(Repository.Params(RequestType.FirebaseRequest.GetUser))
                .collectAndSetState {
                    parseResponse(
                        response = it,
                        onError = { throwable ->
                            logger.e(throwable, "Error: ${throwable.message}")
                        },
                    )
                }
        }
    }
}