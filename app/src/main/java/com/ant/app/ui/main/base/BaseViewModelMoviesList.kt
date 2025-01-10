package com.ant.app.ui.main.base

import androidx.lifecycle.viewModelScope
import com.ant.analytics.AnalyticsEvent
import com.ant.analytics.AnalyticsHelper
import com.ant.analytics.CrashlyticsHelper
import com.ant.ui.viewmodels.BaseViewModel
import com.ant.app.ui.extensions.updateMoviesStatePages
import com.ant.common.flow.CustomStateFlow
import com.ant.common.flow.MutableCustomStateFlow
import com.ant.common.logger.TmdbLogger
import com.ant.domain.usecases.UseCase
import com.ant.models.model.UIState
import com.ant.models.model.get
import com.ant.models.request.RequestType
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import logAnalytics
import javax.inject.Inject

abstract class BaseViewModelMoviesList<P : RequestType, R>(
    val logger: TmdbLogger,
    val useCase: UseCase<P, List<R>>,

    ) : BaseViewModel<UIState<List<R>>>(UIState()) {

    @Inject
    lateinit var crashlyticsHelper: CrashlyticsHelper

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    private val _currentPage = MutableCustomStateFlow<Int>()
    val currentPage: CustomStateFlow<Int> = _currentPage

    private val _pageSize = MutableCustomStateFlow<Int>()
    val pageSize: CustomStateFlow<Int> = _pageSize

    init {
        _currentPage.onEach {
            logger.d("Emitting page: $it")
            loadPage(it)
        }.launchIn(viewModelScope)

        viewModelScope.launch {
            logger.d("Emitting first page.")

            _currentPage.setValue(FIRST_PAGE)
        }
    }

    fun loadNextPage() {
        viewModelScope.launch {
            val nextPage = (_currentPage.getValue() ?: 1) + 1
            logger.d("Emitting next page: $nextPage")
            _currentPage.setValue(nextPage)
        }
    }

    fun refresh() {
        viewModelScope.launch {
            logger.d("refreshing")
            _currentPage.setValue(FIRST_PAGE)
        }
    }

    private suspend fun loadPage(page: Int = FIRST_PAGE) {
        useCase(getParams(page)).collectAndSetState {
            it.get()?.let { data ->
                _pageSize.setValue(data.size)
            }
            updateMoviesStatePages(newResult = it,
                oldData = stateAsFlow.value.data,
                page = page,
                onError = { error ->
                    crashlyticsHelper.logError(error)
                },
                onSuccess = { success ->
                    analyticsHelper.logAnalytics(
                        type = AnalyticsEvent.Types.MOVIE_LIST_SCREEN,
                        key = AnalyticsEvent.ParamKeys.MOVIE_LIST_TYPE,
                        value = success?.toString()
                    )
                })
        }
    }

    private fun getParams(page: Int): P {
        return getRequest() //todo fix page
    }

    abstract fun getRequest(): P

    companion object {
        const val FIRST_PAGE = 1
    }
}