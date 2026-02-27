package com.ant.feature.tvshow.category

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ant.domain.usecases.tvseries.TvShowListUseCase
import com.ant.models.model.Result
import com.ant.models.request.RequestType
import com.ant.models.request.TvShowType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvShowCategoryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val tvShowListUseCase: TvShowListUseCase,
) : ViewModel() {

    private val categoryTypeString: String = checkNotNull(savedStateHandle["categoryType"])
    private val tvShowType: TvShowType = TvShowType.valueOf(categoryTypeString)

    private val _uiState = MutableStateFlow(TvShowCategoryUiState(categoryType = tvShowType))
    val uiState: StateFlow<TvShowCategoryUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadTvShows()
    }

    fun refresh() {
        loadTvShows(isRefresh = true)
    }

    private fun loadTvShows(isRefresh: Boolean = false) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            val request = RequestType.TvShowRequest(tvSeriesType = tvShowType, page = 1)
            tvShowListUseCase(request).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update {
                            if (isRefresh) it.copy(isRefreshing = true)
                            else it.copy(isLoading = true)
                        }
                    }

                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                tvShows = result.data,
                                isLoading = false,
                                isRefreshing = false,
                                error = null,
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isRefreshing = false,
                                error = result.throwable.message,
                            )
                        }
                    }
                }
            }
        }
    }
}
