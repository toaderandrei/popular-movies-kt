package com.ant.app.ui.main.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ant.resources.R as R2
import com.ant.app.databinding.FragmentMoviesBinding
import com.ant.app.ui.main.base.NavigationFragment
import com.ant.common.extensions.doOnSizeChange
import com.ant.common.logger.TmdbLogger
import com.ant.epoxy.extensions.init
import com.ant.models.entities.MovieData
import com.ant.models.model.MoviesState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MoviesFragment : NavigationFragment<MoviesViewModel, FragmentMoviesBinding>() {

    override val viewModel: MoviesViewModel by viewModels()

    @Inject
    lateinit var logger: TmdbLogger

    @Inject
    internal lateinit var controller: MoviesEpoxyController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initControllerCallbacks()
        initBindings()
    }

    private fun initControllerCallbacks() {
        controller.callbacks = object : MoviesEpoxyController.Callbacks {
            override fun onPopularMoviesClicked() {
                findNavController().navigate(MoviesFragmentDirections.toPopular())
            }

            override fun onTopMoviesClicked() {
                findNavController().navigate(MoviesFragmentDirections.toToprated())
            }

            override fun onNowPlayingMoviesClicked() {
                findNavController().navigate(MoviesFragmentDirections.toNowplaying())
            }

            override fun onUpComingMoviesClicked() {
                findNavController().navigate(MoviesFragmentDirections.toUpcoming())
            }

            override fun onItemClicked(viewHolderId: Long, item: MovieData) {
                showDetailsScreen(item)
            }
        }

        with(viewModel) {
            viewModel.refresh()
            stateAsLiveData.observe(viewLifecycleOwner, ::renderModels)
        }
    }

    private fun renderModels(moviesState: MoviesState?) {
        moviesState?.let {
            controller.state = it
            binding.state = it
        }
    }


    private fun initBindings() {
        with(binding) {
            toolbar.let {
                it.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R2.id.search -> {
                            logger.d("open search")
                            true
                        }
                        R2.id.user_login, R2.id.user_avatar -> {
                            logger.d("open account")
                            true
                        }
                        else -> {
                            false
                        }
                    }
                }
            }

            moviesRv.init(controller)
            moviesAppBar.doOnSizeChange {
                moviesRv.updatePadding(top = it.height)
                // todo - verify and test this.
                moviesSwipeRefresh.setProgressViewOffset(
                    true, 0, it.height + moviesSwipeRefresh.progressCircleDiameter
                )
                true
            }

            moviesSwipeRefresh.setOnRefreshListener {
                viewModel.refresh()
                moviesSwipeRefresh.postOnAnimation {
                    moviesSwipeRefresh.isRefreshing = false
                }
            }
        }
    }

    private fun showDetailsScreen(movieData: MovieData) {
        findNavController().navigate(MoviesFragmentDirections.toDetails(movieData.id))
    }

    override fun createViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentMoviesBinding {
        return FragmentMoviesBinding.inflate(inflater, container, false)
    }

    override fun getThisFragment(): Fragment {
        return this@MoviesFragment
    }
}