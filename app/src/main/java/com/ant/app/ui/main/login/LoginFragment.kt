package com.ant.app.ui.main.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ant.app.databinding.FragmentLoginUserBinding
import com.ant.app.ui.main.base.NavigationFragment
import com.ant.common.extensions.observe
import com.ant.common.listeners.LoginCallback
import com.ant.models.model.UIState
import com.ant.models.model.UserData
import com.ant.models.model.errorMessage
import com.ant.models.model.isError
import com.ant.models.model.isLoading
import com.ant.models.model.isSuccess
import dagger.hilt.android.AndroidEntryPoint
import com.ant.resources.R as R2

@AndroidEntryPoint
class LoginFragment : NavigationFragment<LoginViewModelOld, FragmentLoginUserBinding>() {

    override val viewModel: LoginViewModelOld by viewModels()
    override fun createViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentLoginUserBinding {
        return FragmentLoginUserBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            loginContent.loginCallback = object : LoginCallback {
                override fun login() {
                    logger.d("Account login.")
                    val username = loginContent.username
                    val password = loginContent.password
                    if (username.isNullOrEmpty() || password.isNullOrEmpty()) {
                        logger.d("Username or password is empty.")
                        updateUiElements(
                            loading = false,
                            loadingError = true,
                            errorMessage = getString(R2.string.username_or_password_empty)
                        )
                        return
                    }

                    viewModel.login(username, password)
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
            stateAsFlow.observe(viewLifecycleOwner, ::updateUi)
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                with(binding) {
                    loginContent.tvError.text = null
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    private fun updateUi(loginState: UIState<UserData>) {
        with(binding) {
            when {
                loginState.isLoading -> {
                    logger.d("Loading...")
                    updateUiElements(loading = true)
                }

                loginState.isSuccess -> {
                    logger.d("Success loading profile: $loginState.")
                    val data = loginState.data
                    if (!data?.sessionId.isNullOrBlank()) {
                        updateUiElements(loggedIn = true)
                        val formattedString =
                            getString(R2.string.username_account, data?.username)
                        loginContent.tvUsernameLoggedInTmdb.text = formattedString
                    } else {
                        // success logging in but invalid session id. should not happen.
                        updateUiElements(
                            loadingError = true,
                            errorMessage = getString(R2.string.unknown_error)
                        )
                    }
                }

                loginState.isError -> {
                    logger.d("Error logging in: ${loginState.errorMessage}")
                    updateUiElements(
                        loadingError = true,
                        errorMessage = loginState.errorMessage ?: getString(R2.string.unknown_error)
                    )
                }
            }
        }
    }

    private fun FragmentLoginUserBinding.updateUiElements(
        loading: Boolean = false,
        loggedIn: Boolean = false,
        loadingError: Boolean = false,
        errorMessage: String? = null,
    ) {
        isLoading = loading
        isTmdbApiLoggedIn = loggedIn
        isLoadingError = loadingError
        errorMessage?.let {
            val formattedString =
                getString(R2.string.error_login, errorMessage)
            loginContent.tvError.text = formattedString
        }
    }

    override fun getThisFragment(): Fragment {
        return this@LoginFragment
    }
}