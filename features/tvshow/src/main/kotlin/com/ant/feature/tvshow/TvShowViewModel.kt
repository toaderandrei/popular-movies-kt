package com.ant.feature.tvshow

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ant.domain.usecases.tvseries.TvShowListUseCase
import com.ant.models.model.Result
import com.ant.models.request.TvShowType
import com.ant.models.request.RequestType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvShowViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val tvShowListUseCase: TvShowListUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TvShowUiState())
    val uiState: StateFlow<TvShowUiState> = _uiState.asStateFlow()

    init {
        loadTvShows()
    }

    fun onCategoryChange(category: TvShowType) {
        _uiState.update { it.copy(selectedCategory = category) }
        loadTvShows()
    }

    fun refresh() {
        loadTvShows(isRefresh = true)
    }

    private fun loadTvShows(isRefresh: Boolean = false) {
        viewModelScope.launch {
            val request = RequestType.TvShowRequest(
                tvSeriesType = _uiState.value.selectedCategory,
                page = 1
            )

            tvShowListUseCase(request).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = !isRefresh,
                                isRefreshing = isRefresh,
                                error = null
                            )
                        }
                    }

                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isRefreshing = false,
                                tvShows = result.data,
                                error = null
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isRefreshing = false,
                                error = result.throwable.message ?: "An error occurred"
                            )
                        }
                    }
                }
            }
        }
    }
}
