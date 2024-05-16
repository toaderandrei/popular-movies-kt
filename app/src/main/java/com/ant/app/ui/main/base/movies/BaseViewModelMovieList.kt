package com.ant.app.ui.main.base.movies

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ant.app.ui.base.BaseViewModel
import com.ant.common.logger.TmdbLogger
import com.ant.domain.usecases.movies.MovieListUseCase
import com.ant.models.entities.MovieData
import com.ant.models.model.MoviesListState
import com.ant.models.model.Result
import com.ant.models.model.isLoading
import com.ant.models.model.isSuccess
import com.ant.models.request.RequestType
import com.ant.models.source.repositories.Repository
import kotlinx.coroutines.launch

abstract class BaseViewModelMovieList(
    val logger: TmdbLogger,
    val useCase: MovieListUseCase,
) : BaseViewModel<MoviesListState>(
    MoviesListState()
) {
    private val _currentPage = MutableLiveData<Int>().apply { value = 0 }
    val currentPage = MediatorLiveData<Int>()

    init {
        currentPage.addSource(_currentPage) { result ->
            logger.d("loading page: $result")
            loadPage(result)
            currentPage.value = result
        }
    }

    fun loadNextPage() {
        _currentPage.value = _currentPage.value?.let { it + FIRST_PAGE } ?: FIRST_PAGE
    }

    fun refresh() {
        logger.d("refreshing")
        _currentPage.value = FIRST_PAGE
    }

    private fun loadPage(page: Int = FIRST_PAGE) {
        viewModelScope.launch {
            useCase(
                parameters = getMovieParams(page)
            ).collectAndSetState {
                logger.d("state: $this")
                parseResponse(it)
            }
        }
    }

    private fun getMovieParams(page: Int): Repository.Params<RequestType.MovieRequest> {
        return Repository.Params(
            getMovieRequest(),
            page = page
        )
    }

    abstract fun getMovieRequest(): RequestType.MovieRequest

    private fun MoviesListState.parseResponse(it: Result<List<MovieData>>) = if (it.isLoading) {
        copy(loading = true, items = null, error = null)
    } else if (it.isSuccess) {
        copy(loading = false, items = it.get(), error = null)
    } else {
        copy(loading = false, items = null, error = (it as Result.Error).throwable)
    }

    companion object {
        const val FIRST_PAGE = 1
    }
}