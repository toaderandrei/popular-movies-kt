package com.ant.app.ui.main.tvseries

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ant.resources.R as R2
import com.ant.app.databinding.FragmentTvshowBinding
import com.ant.app.ui.main.base.NavigationFragment
import com.ant.app.ui.main.movies.MoviesFragment
import com.ant.common.extensions.doOnSizeChange
import com.ant.epoxy.extensions.init
import com.ant.models.entities.TvShow
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TvShowFragment : NavigationFragment<TvShowViewModel, FragmentTvshowBinding>() {

    override val viewModel: TvShowViewModel by viewModels()

    @Inject
    internal lateinit var controller: TvShowEpoxyController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBindings()
        initControllerCallbacks()
    }

    private fun initControllerCallbacks() {
        controller.callbacks = object : TvShowEpoxyController.Callbacks {
            override fun onPopularTvSeriesClicked() {
                findNavController().navigate(TvShowFragmentDirections.toPopular())
            }

            override fun onTopRatedTvSeriesClicked() {
                findNavController().navigate(TvShowFragmentDirections.toToprated())
            }

            override fun onAiringTodayTvSeriesClicked() {
                findNavController().navigate(TvShowFragmentDirections.toAiringToday())
            }

            override fun onTvNowTvSeriesClicked() {
                findNavController().navigate(TvShowFragmentDirections.toNowOntv())
            }

            override fun onItemClicked(viewHolderId: Long, item: TvShow) {
                showDetailsScreen(item)
            }
        }

        viewModel.stateAsLiveData.observe(viewLifecycleOwner, ::renderModels)
    }

    private fun initBindings() {
        with(binding) {
            toolbar.let {
                it.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R2.id.search -> {
                            Log.d(MoviesFragment::class.java.simpleName, "open search")
                            // openSearch()
                            true
                        }
                        R2.id.user_login, R2.id.user_avatar -> {
                            Log.d(MoviesFragment::class.java.simpleName, "open account")
                            // findNavController().navigate(R.id.navigation_account)
                            true
                        }
                        else -> {
                            false
                        }
                    }
                }
            }

            tvseriesRv.init(controller)
            tvseriesAppBar.doOnSizeChange {
                tvseriesRv.updatePadding(top = it.height)
                // todo - verify and test this.
                tvseriesSwipeRefresh.setProgressViewOffset(
                    true, 0, it.height + binding.tvseriesSwipeRefresh.progressCircleDiameter
                )
                true
            }

            tvseriesSwipeRefresh.setOnRefreshListener {
                viewModel.refresh()
                binding.tvseriesSwipeRefresh.postOnAnimation {
                    binding.tvseriesSwipeRefresh.isRefreshing = false
                }
            }
        }
    }

    private fun renderModels(moviesState: TvShowState?) {
        moviesState?.let {
            controller.state = it
            binding.state = it
        }
    }

    private fun showDetailsScreen(tvShow: TvShow) {
        findNavController().navigate(TvShowFragmentDirections.toDetails(tvShow.id))
    }

    override fun createViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentTvshowBinding {
        return FragmentTvshowBinding.inflate(inflater, container, false)
    }

    override fun getThisFragment(): Fragment {
        return this@TvShowFragment
    }
}