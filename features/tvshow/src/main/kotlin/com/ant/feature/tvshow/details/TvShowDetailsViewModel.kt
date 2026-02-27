package com.ant.feature.tvshow.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ant.domain.usecases.tvseries.DeleteTvSeriesDetailsUseCase
import com.ant.domain.usecases.tvseries.SaveTvSeriesDetailsUseCase
import com.ant.domain.usecases.tvseries.TvSeriesDetailsUseCase
import com.ant.models.model.Result
import com.ant.models.request.RequestType
import com.ant.models.request.TvSeriesAppendToResponseItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvShowDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val tvSeriesDetailsUseCase: TvSeriesDetailsUseCase,
    private val saveTvSeriesDetailsUseCase: SaveTvSeriesDetailsUseCase,
    private val deleteTvSeriesDetailsUseCase: DeleteTvSeriesDetailsUseCase,
) : ViewModel() {

    private val tvShowId: Long = checkNotNull(savedStateHandle["tvShowId"])

    private val _uiState = MutableStateFlow(TvShowDetailsUiState())
    val uiState: StateFlow<TvShowDetailsUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null
    private var toggleJob: Job? = null

    init {
        loadDetails()
    }

    fun retry() = loadDetails()

    fun toggleFavorite() {
        val details = _uiState.value.tvShowDetails ?: return
        val wasFavorite = _uiState.value.isFavorite
        // Optimistic UI update
        _uiState.update { it.copy(isFavorite = !wasFavorite) }
        toggleJob?.cancel()
        toggleJob = viewModelScope.launch {
            val result = if (wasFavorite) {
                deleteTvSeriesDetailsUseCase(details)
            } else {
                saveTvSeriesDetailsUseCase(details)
            }
            result.collect { emission ->
                if (emission is Result.Error) {
                    // Revert on failure
                    _uiState.update { it.copy(isFavorite = wasFavorite) }
                }
            }
        }
    }

    private fun loadDetails() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            val request = RequestType.TvSeriesRequestDetails(
                tmdbTvSeriesId = tvShowId,
                appendToResponseItems = listOf(
                    TvSeriesAppendToResponseItem.VIDEOS,
                    TvSeriesAppendToResponseItem.CREDITS,
                )
            )
            tvSeriesDetailsUseCase(request).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true, error = null) }
                    }

                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                tvShowDetails = result.data,
                                isFavorite = result.data.tvSeriesData.favored == true,
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.throwable.message,
                            )
                        }
                    }
                }
            }
        }
    }
}
