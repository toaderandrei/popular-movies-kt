package com.ant.app.ui.main.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ant.app.databinding.FragmentFavoritesBinding
import com.ant.app.ui.main.base.NavigationFragment
import com.ant.common.extensions.doOnSizeChange
import com.ant.common.logger.TmdbLogger
import com.ant.epoxy.extensions.init
import com.ant.models.entities.MovieData
import com.ant.models.entities.TvShow
import com.ant.models.model.FavoritesState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoritesFragment : NavigationFragment<FavoritesViewModel, FragmentFavoritesBinding>() {

    override val viewModel: FavoritesViewModel by viewModels()

    @Inject
    internal lateinit var controller: FavoritesEpoxyController


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initControllerCallbacks()
        initBindings()
    }

    fun initControllerCallbacks() {
        controller.callbacks = object : FavoritesEpoxyController.Callbacks {
            override fun onTvSeriesItemClicked(viewHolderId: Long, item: TvShow) {
                showTvSeriesDetailsScreen(item)
            }

            override fun onMoviesItemClicked(viewHolderId: Long, item: MovieData) {
                showMovieDetailsScreen(item)
            }
        }

        with(viewModel) {
            viewModel.refresh()
            stateAsLiveData.observe(viewLifecycleOwner, ::renderModels)
        }
    }

    private fun renderModels(favoritesState: FavoritesState?) {
        favoritesState?.let {
            controller.state = it
            binding.state = it
        }
    }

    fun initBindings() {
        with(binding) {
            logger.d("initialize bindings")
            favoritesRv.init(controller)
            favoritesAppBar.doOnSizeChange {
                favoritesRv.updatePadding(top = it.height)
                // todo - verify and test this.
                favoritesSwipeRefresh.setProgressViewOffset(
                    true, 0, it.height + favoritesSwipeRefresh.progressCircleDiameter
                )
                true
            }

            favoritesSwipeRefresh.setOnRefreshListener {
                viewModel.refresh()
                favoritesSwipeRefresh.postOnAnimation {
                    favoritesSwipeRefresh.isRefreshing = false
                }
            }
        }
    }

    override fun createViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentFavoritesBinding {
        return FragmentFavoritesBinding.inflate(inflater, container, false)
    }

    override fun getThisFragment(): Fragment {
        return this@FavoritesFragment
    }

    private fun showMovieDetailsScreen(movieData: MovieData) {
        findNavController().navigate(FavoritesFragmentDirections.toMovieDetails(movieData.id))
    }

    private fun showTvSeriesDetailsScreen(tvShow: TvShow) {
        findNavController().navigate(FavoritesFragmentDirections.toTvshowDetails(tvShow.id))
    }
}