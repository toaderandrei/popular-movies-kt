package com.ant.app.ui.main.base

import androidx.lifecycle.viewModelScope
import com.ant.app.ui.base.BaseViewModel
import com.ant.common.logger.TmdbLogger
import com.ant.domain.usecases.UseCase
import com.ant.models.model.MoviesState
import com.ant.models.model.Result
import com.ant.models.request.RequestType
import com.ant.models.source.repositories.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class BaseViewModelMoviesList<P: RequestType, R>(
    val logger: TmdbLogger,
    val useCase: UseCase<Repository.Params<P>, R>
) : BaseViewModel<MoviesState<R>>(MoviesState()) {
    private val _currentPage = MutableStateFlow(FIRST_PAGE)
    val currentPage: StateFlow<Int> = _currentPage

    init {
        _currentPage.onEach {
            loadPage(it)
        }.launchIn(viewModelScope)
    }

    fun loadNextPage() {
        _currentPage.value += 1
    }

    fun refresh() {
        logger.d("refreshing")
        _currentPage.value = FIRST_PAGE
    }

    private suspend fun loadPage(page: Int = FIRST_PAGE) {
        useCase(getParams(page)).collectAndSetState {
            logger.d("state: $this")
            parseDataResponse(it)
        }
    }

    abstract fun MoviesState<R>.parseDataResponse(it: Result<R>): MoviesState<R>

    private fun getParams(page: Int): Repository.Params<P> {
        return Repository.Params(
            getRequest(),
            page = page
        )
    }

    abstract fun getRequest(): P

    companion object {
        const val FIRST_PAGE = 1
    }
}