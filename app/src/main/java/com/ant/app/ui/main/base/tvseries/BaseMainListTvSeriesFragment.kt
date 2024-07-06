package com.ant.app.ui.main.base.tvseries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.navigation.fragment.findNavController
import com.ant.app.databinding.FragmentListTvshowBinding
import com.ant.app.ui.adapters.TvSeriesListAdapter
import com.ant.app.ui.main.base.NavigationFragment
import com.ant.common.decorator.MarginItemDecoration
import com.ant.common.extensions.doOnSizeChange
import com.ant.common.extensions.observe
import com.ant.common.listeners.OnScrollCallback
import com.ant.common.listeners.RecyclerViewScrollListener
import com.ant.common.listeners.RetryCallback
import com.ant.common.logger.TmdbLogger
import com.ant.models.entities.TvShow
import com.ant.models.model.MoviesState
import javax.inject.Inject

abstract class BaseMainListTvSeriesFragment<VIEW_MODEL : BaseViewModelTvShowList> :
    NavigationFragment<VIEW_MODEL, FragmentListTvshowBinding>(), OnScrollCallback {

    private lateinit var rvAdapter: TvSeriesListAdapter
    private lateinit var recyclerViewScrollListener: RecyclerViewScrollListener

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvAdapter = TvSeriesListAdapter(callback = {
            logger.d("clicked on movie: ${it.tvSeriesName}")
            showDetailsScreen(it)
        })

        recyclerViewScrollListener = RecyclerViewScrollListener(this)

        with(binding) {
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            tvseriesGridAppBar.doOnSizeChange {
                tvseriesGridRv.updatePadding(top = it.height)
                tvseriesGridSwipeRefresh.setProgressViewOffset(
                    true, 0, it.height + binding.tvseriesGridSwipeRefresh.progressCircleDiameter / 2
                )
                true
            }

            moviesLoadingStateId.callback = object : RetryCallback {
                override fun retry() {
                    viewModel.refresh()
                }
            }

            tvseriesGridSwipeRefresh.setOnRefreshListener(viewModel::refresh)
            tvseriesGridRv.adapter = rvAdapter
            tvseriesGridRv.addOnScrollListener(recyclerViewScrollListener)
            // Add margin decoration to the movies recycler view.
            tvseriesGridRv.apply {
                addItemDecoration(MarginItemDecoration(paddingLeft))
            }
        }

        with(viewModel) {
            stateAsFlow.observe(viewLifecycleOwner, ::showData)
            currentPage.observe(viewLifecycleOwner) {
                logger.d("loading page:$it")
            }
        }
    }

    override fun createViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentListTvshowBinding {
        return FragmentListTvshowBinding.inflate(inflater, container, false)
    }

    private fun showData(tvSeriesData: MoviesState<List<TvShow>?>) {
        with(tvSeriesData) {
            logger.d("showData: ${tvSeriesData.data}")
            binding.tvseriesGridSwipeRefresh.isRefreshing = loading
            recyclerViewScrollListener.isLoading.value = loading
            binding.moviesLoadingStateId.isError = error != null
            binding.moviesLoadingStateId.errorMsg.error = error?.message

            tvSeriesData.data?.let {
                logger.d("items to load: ${it.size}")
                val newList = ArrayList(rvAdapter.currentList)
                newList.addAll(it)
                submitList(newList)
            }
        }
    }

    private fun submitList(newList: List<TvShow>) {
        rvAdapter.submitList(newList)
    }

    abstract fun showDetailsScreen(tvShow: TvShow)

    override fun onScrollUpdate() {
        logger.d("Scroll update triggered.")
        viewModel.loadNextPage()
    }
}