package com.ant.app.ui.main.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ant.app.databinding.FragmentLoginUserBinding
import com.ant.app.ui.main.base.NavigationFragment
import com.ant.common.listeners.ClickCallback
import com.ant.common.listeners.LoginCallback
import com.ant.models.entities.LoginSession
import com.ant.models.model.MoviesState
import com.ant.models.model.errorMessage
import com.ant.models.model.isLoading
import com.ant.models.model.isSuccess
import dagger.hilt.android.AndroidEntryPoint
import com.ant.resources.R as R2

@AndroidEntryPoint
class LoginFragment : NavigationFragment<LoginViewModel, FragmentLoginUserBinding>() {

    override val viewModel: LoginViewModel by viewModels()

    override fun createViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentLoginUserBinding {
        return FragmentLoginUserBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            loginContent.callback = object : ClickCallback {
                override fun click() {
                    logger.d("Show firebase login screen.")
                }
            }

            loginContent.loginCallback = object : LoginCallback {
                override fun login() {
                    logger.d("Account login.")
                    val username = loginContent.username
                    val password = loginContent.password
                    if (username.isNullOrEmpty() || password.isNullOrEmpty()) {
                        logger.d("Username or password is empty.")
                        loginContent.loadingErrorMessage =
                            context?.getString(R2.string.unknown_error)
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
        with(binding) {
            when {
                loginState.isLoading -> {
                    logger.d("Loading...")
                    isLoading = true

                }

                loginState.isSuccess -> {
                    logger.d("Success loading profile: $loginState.")
                    loginState.data?.let {
                        logger.d("User: ${it.sessionId}")
                        isTmdbApiLoggedIn = true
                        val formattedString =
                            getString(R2.string.username_tmdb_logged_in, loginContent.username)
                        loginContent.tvUsernameLoggedInTmdb.text = formattedString
                    }
                }

                else -> {
                    logger.d("Error logging in: ${loginState.errorMessage}")
                    isLoading = false
                    loginState.errorMessage?.let {
                        loginContent.loadingErrorMessage = it
                    }
                    isTmdbApiLoggedIn = false
                }
            }
        }

    }

    override fun getThisFragment(): Fragment {
        return this@LoginFragment
    }
}