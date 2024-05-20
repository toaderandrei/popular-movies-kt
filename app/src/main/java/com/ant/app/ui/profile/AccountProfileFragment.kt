package com.ant.app.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ant.app.databinding.FragmentAccountProfileBinding
import com.ant.app.ui.base.BaseFragment
import com.ant.common.listeners.AccountLoginCallback
import com.ant.common.logger.TmdbLogger
import com.ant.models.model.MoviesState
import com.ant.models.model.isLoading
import com.ant.models.model.isSuccess
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AccountProfileFragment(
) : BaseFragment<AccountProfileViewModel, FragmentAccountProfileBinding>() {
    override val viewModel: AccountProfileViewModel by viewModels()

    @Inject
    lateinit var logger: TmdbLogger

    override fun createViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentAccountProfileBinding {
        return FragmentAccountProfileBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewModel) {
            stateAsLiveData.observe(viewLifecycleOwner, ::updateUi)
        }

        with(binding) {
            loginCallback = object : AccountLoginCallback {
                override fun login() {
                    logger.d("Account login.")
                    findNavController().navigate(AccountProfileFragmentDirections.toLogin())
                }

                override fun logout() {
                    logger.d("Account logout.")
                }
            }
        }
    }

    private fun updateUi(loginState: MoviesState<Boolean>?) {

        loginState?.let {
            if (loginState.isLoading) {
                logger.d("Loading...")
            } else if (loginState.isSuccess) {
                logger.d("Success loading profile: $loginState.")
                with(binding) {
                    userLoggedIn = loginState.data ?: false
                }
            } else {
                logger.d("Error.")
            }
        }
    }

    override fun getThisFragment(): Fragment {
        return this@AccountProfileFragment
    }
}