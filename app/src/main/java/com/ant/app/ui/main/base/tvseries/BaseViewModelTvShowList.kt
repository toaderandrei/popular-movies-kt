package com.ant.app.ui.main.base.tvseries

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ant.app.ui.base.BaseViewModel
import com.ant.app.ui.extensions.parseResponse
import com.ant.app.ui.main.base.movies.BaseViewModelMovieList
import com.ant.common.logger.TmdbLogger
import com.ant.domain.usecases.tvseries.TvShowListUseCase
import com.ant.models.entities.TvShow
import com.ant.models.model.MoviesState
import com.ant.models.request.RequestType
import com.ant.models.source.repositories.Repository
import kotlinx.coroutines.launch

abstract class BaseViewModelTvShowList(
    val logger: TmdbLogger,
    val useCase: TvShowListUseCase,
) : BaseViewModel<MoviesState<List<TvShow>?>>(
    MoviesState()
) {
    private val _currentPage = MutableLiveData<Int>().apply { value = 0 }
    val currentPage = MediatorLiveData<Int>()

    init {
        currentPage.addSource(_currentPage) { result ->
            logger.d("loading page: $result")
            loadPage(result)
            currentPage.value = result
        }
        refresh()
    }

    fun loadNextPage() {
        _currentPage.value = _currentPage.value?.let { it + BaseViewModelMovieList.FIRST_PAGE }
            ?: BaseViewModelMovieList.FIRST_PAGE
    }

    fun refresh() {
        logger.d("refreshing")
        _currentPage.value = BaseViewModelMovieList.FIRST_PAGE
    }

    private fun loadPage(page: Int = FIRST_PAGE) {
        viewModelScope.launch {
            useCase(
                parameters = getMovieParams(page)
            ).collectAndSetState {
                logger.d("state: $this")
                parseResponse(
                    response = it,
                    onError = { throwable ->
                        logger.e(throwable, "Error: ${throwable.message}")
                    }
                )
            }
        }
    }

    private fun getMovieParams(page: Int): Repository.Params<RequestType.TvShowRequest> {
        return Repository.Params(
            getTvSeriesRequest(),
            page = page
        )
    }

    abstract fun getTvSeriesRequest(): RequestType.TvShowRequest

    companion object {
        const val FIRST_PAGE = 1
    }
}