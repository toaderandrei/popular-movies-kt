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
        loadAllCategories()
    }

    fun refresh() {
        loadAllCategories(isRefresh = true)
    }

    /**
     * Load TV shows for all categories to display in sections
     */
    private fun loadAllCategories(isRefresh: Boolean = false) {
        val categories = listOf(
            TvShowType.POPULAR,
            TvShowType.TOP_RATED,
            TvShowType.AIRING_TODAY,
            TvShowType.ONTV_NOW
        )

        if (!isRefresh) {
            _uiState.update { it.copy(isLoading = true) }
        } else {
            _uiState.update { it.copy(isRefreshing = true) }
        }

        categories.forEach { category ->
            loadCategoryTvShows(category, isRefresh)
        }
    }

    /**
     * Load TV shows for a specific category
     */
    private fun loadCategoryTvShows(category: TvShowType, isRefresh: Boolean = false) {
        viewModelScope.launch {
            val request = RequestType.TvShowRequest(
                tvSeriesType = category,
                page = 1
            )

            tvShowListUseCase(request).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update { currentState ->
                            val updatedSections = currentState.tvShowSections.toMutableMap()
                            updatedSections[category] = TvShowSection(
                                category = category,
                                isLoading = true
                            )
                            currentState.copy(tvShowSections = updatedSections)
                        }
                    }

                    is Result.Success -> {
                        _uiState.update { currentState ->
                            val updatedSections = currentState.tvShowSections.toMutableMap()
                            updatedSections[category] = TvShowSection(
                                category = category,
                                tvShows = result.data.items,
                                isLoading = false
                            )
                            currentState.copy(
                                tvShowSections = updatedSections,
                                isLoading = false,
                                isRefreshing = false,
                                error = null
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update { currentState ->
                            val updatedSections = currentState.tvShowSections.toMutableMap()
                            updatedSections[category] = TvShowSection(
                                category = category,
                                isLoading = false,
                                error = result.throwable.message
                            )
                            currentState.copy(
                                tvShowSections = updatedSections,
                                isLoading = false,
                                isRefreshing = false
                            )
                        }
                    }
                }
            }
        }
    }
}
