package com.ant.app.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ant.app.databinding.FragmentLoginUserBinding
import com.ant.app.ui.base.BaseFragment
import com.ant.common.listeners.LoginCallback
import com.ant.common.logger.TmdbLogger
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginUserBinding>() {

    override val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var logger: TmdbLogger

    override fun createViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentLoginUserBinding {
        return FragmentLoginUserBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            loginCallback = object : LoginCallback {
                override fun login() {
                    logger.d("Account login.")
                    if (username.isNullOrEmpty() || password.isNullOrEmpty()) {
                        logger.d("Username or password is empty.")
                        loadingState.errorMsg.error = "Username or password is empty."
                        return
                    }
                    viewModel.authenticateUser(edtUsername.toString(), edtPassword.toString())
                }

                override fun logout() {
                    logger.d("Account logout.")
                    viewModel.logout()
                }

                override fun signUp() {
                    logger.d("Account sign up.")
                    viewModel.signUp()
                }
            }
        }
    }

    override fun getThisFragment(): Fragment {
        return this@LoginFragment
    }
}