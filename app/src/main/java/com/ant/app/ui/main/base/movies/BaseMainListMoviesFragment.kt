package com.ant.app.ui.main.base.movies

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ant.app.databinding.FragmentListMoviesBinding
import com.ant.app.ui.adapters.MovieListAdapter
import com.ant.app.ui.main.base.BaseViewModelMoviesList
import com.ant.app.ui.main.base.BaseViewModelMoviesList.Companion.FIRST_PAGE
import com.ant.app.ui.main.base.NavigationFragment
import com.ant.common.decorator.MarginItemDecoration
import com.ant.common.extensions.doOnSizeChange
import com.ant.common.extensions.observe
import com.ant.common.listeners.OnScrollCallback
import com.ant.common.listeners.RecyclerViewScrollListener
import com.ant.common.listeners.RetryCallback
import com.ant.models.entities.MovieData
import com.ant.models.model.UIState
import com.ant.models.model.isError
import com.ant.models.model.isLoading

abstract class BaseMainListMoviesFragment<VIEW_MODEL : BaseViewModelMoviesList<*, MovieData>> :
    NavigationFragment<VIEW_MODEL, FragmentListMoviesBinding>(), OnScrollCallback {

    private lateinit var movieListAdapter: MovieListAdapter
    private lateinit var recyclerViewScrollListener: RecyclerViewScrollListener
    private var recyclerViewState: Parcelable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieListAdapter = MovieListAdapter(callback = {
            logger.d("clicked on movie: ${it.name}")
            showDetailsScreen(it)
        })
        movieListAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        recyclerViewScrollListener = RecyclerViewScrollListener(this)

        with(binding) {
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            // Appbar
            moviesGridAppBar.doOnSizeChange {
                moviesGridRv.updatePadding(top = it.height)
                moviesGridSwipeRefresh.setProgressViewOffset(
                    true, 0, it.height + binding.moviesGridSwipeRefresh.progressCircleDiameter / 2
                )
                true
            }

            moviesLoadingStateId.callback = object : RetryCallback {
                override fun retry() {
                    viewModel.refresh()
                }
            }

            // refresh listener.
            moviesGridSwipeRefresh.setOnRefreshListener(viewModel::refresh)
            moviesGridRv.adapter = movieListAdapter
            moviesGridRv.addOnScrollListener(recyclerViewScrollListener)
            // Add margin decoration to the movies recycler view.
            moviesGridRv.apply {
                addItemDecoration(MarginItemDecoration(paddingLeft))
            }
        }

        with(viewModel) {
            stateAsFlow.observe(viewLifecycleOwner, ::showData)
            currentPage.observe(viewLifecycleOwner) {
                logger.d("loading page: $it")
                if (it == FIRST_PAGE) {
                    recyclerViewState = null
                }
            }
        }
    }

    abstract fun showDetailsScreen(movieData: MovieData)

    override fun createViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentListMoviesBinding {
        return FragmentListMoviesBinding.inflate(inflater, container, false)
    }

    private fun showData(movieListState: UIState<List<MovieData>>?) {
        movieListState?.let { moviesState ->
            logger.d("showData: movieListState: $moviesState")

            binding.moviesGridSwipeRefresh.isRefreshing = moviesState.isLoading
            recyclerViewScrollListener.isLoading.value = moviesState.isLoading
            binding.moviesLoadingStateId.isError = moviesState.isError
            binding.moviesLoadingStateId.errorMsg.error = moviesState.error?.message

            moviesState.data?.let {
                logger.d("showData items: ${it.size}")
                submitList(it)
            }
        }
    }

    private fun submitList(newList: List<MovieData>) {
        movieListAdapter.loadResults(
            newList,
            pageSize = viewModel.pageSize.getValue() ?: 1,
            viewModel.currentPage.getValue() == 1
        )
    }

    override fun onScrollUpdate() {
        logger.d("Scroll update ended.")
        viewModel.loadNextPage()
    }
}