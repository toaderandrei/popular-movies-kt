package com.ant.app.ui.main.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ant.app.databinding.FragmentAccountProfileBinding
import com.ant.app.ui.base.BaseFragment
import com.ant.common.extensions.observe
import com.ant.common.listeners.AccountLoginCallback
import com.ant.common.logger.TmdbLogger
import com.ant.core.ui.ToolbarNavigationManager
import com.ant.models.model.MoviesState
import com.ant.models.model.UserData
import com.ant.models.model.isError
import com.ant.models.model.isLoading
import com.ant.models.model.isSuccess
import com.ant.resources.R
import com.ant.app.R as R2
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AccountProfileFragment(
) : BaseFragment<AccountProfileViewModel, FragmentAccountProfileBinding>() {
    override val viewModel: AccountProfileViewModel by viewModels()

    @Inject
    lateinit var logger: TmdbLogger

    @Inject
    lateinit var toolbarWithNavigationManager: ToolbarNavigationManager

    override fun createViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentAccountProfileBinding {
        return FragmentAccountProfileBinding.inflate(inflater, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        toolbarWithNavigationManager.attach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewModel) {
            verifyIfUserIsLoggedIn()
            stateAsFlow.observe(viewLifecycleOwner, ::updateUi)
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
        toolbarWithNavigationManager.setupToolbar(view, R2.id.toolbar)
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

    override fun onDetach() {
        super.onDetach()
        toolbarWithNavigationManager.detach()
    }

    override fun getThisFragment(): Fragment {
        return this@AccountProfileFragment
    }
}