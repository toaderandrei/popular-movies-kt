package com.ant.app.ui.profile

import com.ant.app.ui.base.BaseViewModel
import com.ant.domain.usecases.login.LoginUseCase
import com.ant.models.model.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountProfileViewModel @Inject constructor(
    loginUseCase: LoginUseCase,

) : BaseViewModel<LoginState<Boolean>>(LoginState()) {
    init {
        verifyIfUserIsLoggedIn()
    }

    private fun verifyIfUserIsLoggedIn() {

    }
}