package com.ant.app.ui.main.base.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.navigation.fragment.findNavController
import com.ant.app.databinding.FragmentListMoviesBinding
import com.ant.app.ui.adapters.MovieListAdapter
import com.ant.app.ui.main.base.BaseViewModelMoviesList
import com.ant.app.ui.main.base.NavigationFragment
import com.ant.common.decorator.MarginItemDecoration
import com.ant.common.extensions.doOnSizeChange
import com.ant.common.extensions.observe
import com.ant.common.listeners.OnScrollCallback
import com.ant.common.listeners.RecyclerViewScrollListener
import com.ant.common.listeners.RetryCallback
import com.ant.models.entities.MovieData
import com.ant.models.model.MoviesState
import com.ant.models.model.isError
import com.ant.models.model.isLoading
import com.ant.models.request.RequestType

abstract class BaseMainListMoviesFragment<VIEW_MODEL : BaseViewModelMoviesList<*, List<MovieData>>> :
    NavigationFragment<VIEW_MODEL, FragmentListMoviesBinding>(), OnScrollCallback {

    private lateinit var movieListAdapter: MovieListAdapter
    private lateinit var recyclerViewScrollListener: RecyclerViewScrollListener

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieListAdapter = MovieListAdapter(callback = {
            logger.d("clicked on movie: ${it.name}")
            showDetailsScreen(it)
        })

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
            }
        }
    }

    abstract fun showDetailsScreen(movieData: MovieData)

    override fun createViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentListMoviesBinding {
        return FragmentListMoviesBinding.inflate(inflater, container, false)
    }

    private fun showData(movieListState: MoviesState<List<MovieData>>?) {
        movieListState?.let {
            logger.d("showData: movieListState: $it")

            binding.moviesGridSwipeRefresh.isRefreshing = it.isLoading
            recyclerViewScrollListener.isLoading.value = it.isLoading
            binding.moviesLoadingStateId.isError = it.isError
            binding.moviesLoadingStateId.errorMsg.error = it.error?.message

            it.data?.let {
                logger.d("showData items: ${it.size}")
                val newList = ArrayList(movieListAdapter.currentList)
                newList.addAll(it)
                submitList(newList)
            }
        }
    }

    private fun submitList(newList: List<MovieData>) {
        movieListAdapter.submitList(newList)
    }

    override fun onScrollUpdate() {
        logger.d("Scroll update triggered.")
        viewModel.loadNextPage()
    }
}