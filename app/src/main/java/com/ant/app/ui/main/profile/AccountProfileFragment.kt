package com.ant.app.ui.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ant.app.databinding.FragmentAccountProfileBinding
import com.ant.app.ui.main.base.NavigationFragment
import com.ant.common.listeners.AccountLoginCallback
import com.ant.models.model.MoviesState
import com.ant.models.model.UserData
import com.ant.models.model.isError
import com.ant.models.model.isLoading
import com.ant.models.model.isSuccess
import com.ant.resources.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountProfileFragment(
) : NavigationFragment<AccountProfileViewModel, FragmentAccountProfileBinding>() {
    override val viewModel: AccountProfileViewModel by viewModels()

    override fun createViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentAccountProfileBinding {
        return FragmentAccountProfileBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewModel) {
            verifyIfUserIsLoggedIn()
            stateAsLiveData.observe(viewLifecycleOwner, ::updateUi)
        }

        with(binding) {
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            loginCallback = object : AccountLoginCallback {
                override fun login() {
                    logger.d("Account login.")
                    val directions = getLoginDirections()
                    findNavController().navigate(directions)
                }

                override fun logout() {
                    logger.d("Account logout.")
                    viewModel.logout(binding.tvUsername.text.toString())
                }
            }
        }
    }

    private fun getLoginDirections(logout: Boolean = false): AccountProfileFragmentDirections.ToLoginScreen {
        val directions = AccountProfileFragmentDirections.toLoginScreen()
        directions.setLogout(logout)
        return directions
    }

    private fun updateUi(loginState: MoviesState<UserData>) {

        when {
            loginState.isLoading -> {
                logger.d("Loading...")
            }

            loginState.isSuccess -> {
                logger.d("Success loading profile: $loginState.")
                val data = loginState.data
                if (data?.sessionId.isNullOrBlank()) {
                    binding.userLoggedIn = false
                } else {
                    data?.sessionId?.let { sessionId ->
                        binding.userLoggedIn = sessionId.isNotBlank()
                    }
                    data?.username?.let { name ->
                        val formattedString =
                            getString(R.string.username_account, name)
                        binding.tvUsername.text = formattedString
                    }
                }
            }

            loginState.isError -> {
                logger.d("Error.")
            }
        }
    }

    override fun getThisFragment(): Fragment {
        return this@AccountProfileFragment
    }
}