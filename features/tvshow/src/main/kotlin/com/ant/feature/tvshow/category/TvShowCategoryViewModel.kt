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
        loadJob?.cancel()
        _uiState.update {
            it.copy(
                tvShows = emptyList(),
                currentPage = 0,
                totalPages = 1,
                error = null,
            )
        }
        loadTvShows(isRefresh = true)
    }

    fun loadNextPage() {
        if (!_uiState.value.canLoadMore) return
        loadTvShows(page = _uiState.value.currentPage + 1)
    }

    private fun loadTvShows(isRefresh: Boolean = false, page: Int = 1) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            val request = RequestType.TvShowRequest(tvSeriesType = tvShowType, page = page)
            tvShowListUseCase(request).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update {
                            when {
                                isRefresh -> it.copy(isRefreshing = true)
                                page > 1 -> it.copy(isLoadingMore = true)
                                else -> it.copy(isLoading = true)
                            }
                        }
                    }

                    is Result.Success -> {
                        _uiState.update {
                            val newItems = if (page > 1) {
                                (it.tvShows + result.data.items).distinctBy { tvShow -> tvShow.id }
                            } else {
                                result.data.items
                            }
                            it.copy(
                                tvShows = newItems,
                                currentPage = result.data.page,
                                totalPages = result.data.totalPages,
                                isLoading = false,
                                isRefreshing = false,
                                isLoadingMore = false,
                                error = null,
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isRefreshing = false,
                                isLoadingMore = false,
                                error = result.throwable.message,
                            )
                        }
                    }
                }
            }
        }
    }
}
