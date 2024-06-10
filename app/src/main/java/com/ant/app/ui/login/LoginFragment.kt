package com.ant.app.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ant.app.databinding.FragmentLoginUserBinding
import com.ant.app.ui.base.BaseFragment
import com.ant.common.listeners.ClickCallback
import com.ant.common.listeners.LoginCallback
import com.ant.common.logger.TmdbLogger
import com.ant.models.entities.LoginSession
import com.ant.resources.R as R2
import com.ant.models.model.MoviesState
import com.ant.models.model.errorMessage
import com.ant.models.model.isLoading
import com.ant.models.model.isSuccess
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
            callback = object : ClickCallback {
                override fun click() {
                    logger.d("Show firebase login scree.")
                }

            }
            loginCallback = object : LoginCallback {
                override fun login() {
                    logger.d("Account login.")
                    val username = username
                    val password = password
                    if (username.isNullOrEmpty() || password.isNullOrEmpty()) {
                        logger.d("Username or password is empty.")
                        loadingState.errorMsg.error = "Username or password is empty."
                        return
                    }

                    viewModel.authenticateUser(username, password)
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
        with(viewModel) {
            stateAsLiveData.observe(viewLifecycleOwner, ::updateUi)
        }
    }

    private fun updateUi(loginState: MoviesState<LoginSession>) {
        when {
            loginState.isLoading -> {
                logger.d("Loading...")
                with(binding) {
                    isLoading = true
                }
            }

            loginState.isSuccess -> {
                logger.d("Success loading profile: $loginState.")
                with(binding) {
                    loginState.data?.let {
                        logger.d("User: ${it.sessionId}")
                        isTmdbApiLogggedIn = true
                        val formattedString = getString(R2.string.username_tmdb_logged_in, username)
                        tvUsernameLoggedInTmdb.text = formattedString
                    }
                }
            }

            else -> {
                logger.d("Error logging in: ${loginState.errorMessage}")
                with(binding) {
                    isLoading = false
                    loginState.errorMessage?.let {
                        loadingState.errorMsg.error = it
                    }
                    isTmdbApiLogggedIn = false
                }
            }
        }
    }

    override fun getThisFragment(): Fragment {
        return this@LoginFragment
    }
}