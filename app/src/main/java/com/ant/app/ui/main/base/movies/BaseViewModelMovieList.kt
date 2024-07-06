package com.ant.app.ui.main.base.movies

import androidx.lifecycle.viewModelScope
import com.ant.app.ui.base.BaseViewModel
import com.ant.app.ui.extensions.parseResponse
import com.ant.app.ui.main.base.tvseries.BaseViewModelTvShowList
import com.ant.common.logger.TmdbLogger
import com.ant.domain.usecases.movies.MovieListUseCase
import com.ant.models.entities.MovieData
import com.ant.models.model.MoviesState
import com.ant.models.request.RequestType
import com.ant.models.source.repositories.Repository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

abstract class BaseViewModelMovieList(
    val logger: TmdbLogger,
    val useCase: MovieListUseCase,
) : BaseViewModel<MoviesState<List<MovieData>?>>(
    MoviesState()
) {
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
        useCase(
            parameters = getMovieParams(page)
        ).collectAndSetState {
            logger.d("state: $this")
            parseResponse(response = it,
                onError = { throwable ->
                    logger.e(throwable, "Error loading movie list: ${throwable.message}")
                }
            )
        }
    }


    private fun getMovieParams(page: Int): Repository.Params<RequestType.MovieRequest> {
        return Repository.Params(
            getMovieRequest(),
            page = page
        )
    }

    abstract fun getMovieRequest(): RequestType.MovieRequest

    companion object {
        const val FIRST_PAGE = 1
    }
}