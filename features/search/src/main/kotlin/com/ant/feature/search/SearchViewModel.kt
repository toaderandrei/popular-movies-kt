package com.ant.feature.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ant.domain.usecases.movies.MovieListUseCase
import com.ant.domain.usecases.tvseries.TvShowListUseCase
import com.ant.models.model.Result
import com.ant.models.request.MovieType
import com.ant.models.request.TvShowType
import com.ant.models.request.RequestType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val movieListUseCase: MovieListUseCase,
    private val tvShowListUseCase: TvShowListUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    init {
        // Debounce search queries to avoid excessive API calls
        viewModelScope.launch {
            _searchQuery
                .debounce(300)
                .distinctUntilChanged()
                .collect { query ->
                    if (query.isNotBlank()) {
                        performSearch(query)
                    } else {
                        clearResults()
                    }
                }
        }
    }

    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }
        _searchQuery.value = query
    }

    fun onTabChange(tab: SearchTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun clearSearch() {
        _uiState.update { SearchUiState() }
        _searchQuery.value = ""
    }

    private fun clearResults() {
        _uiState.update {
            it.copy(
                isSearching = false,
                movieResults = emptyList(),
                tvShowResults = emptyList(),
                error = null
            )
        }
    }

    private fun performSearch(query: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSearching = true, error = null) }

            // Search movies
            launch {
                val request = RequestType.MovieRequest(
                    movieType = MovieType.POPULAR,
                    page = 1
                )
                movieListUseCase(request).collect { result ->
                    when (result) {
                        is Result.Success -> {
                            _uiState.update { state ->
                                state.copy(
                                    movieResults = result.data.filter {
                                        it.name?.contains(query, ignoreCase = true) == true
                                    },
                                    isSearching = false
                                )
                            }
                        }
                        is Result.Error -> {
                            _uiState.update { state ->
                                state.copy(
                                    isSearching = false,
                                    error = result.throwable.message
                                )
                            }
                        }
                        Result.Loading -> {
                            // Already set isSearching = true above
                        }
                    }
                }
            }

            // Search TV shows
            launch {
                val request = RequestType.TvShowRequest(
                    tvSeriesType = TvShowType.POPULAR,
                    page = 1
                )
                tvShowListUseCase(request).collect { result ->
                    when (result) {
                        is Result.Success -> {
                            _uiState.update { state ->
                                state.copy(
                                    tvShowResults = result.data.filter {
                                        it.name?.contains(query, ignoreCase = true) == true
                                    },
                                    isSearching = false
                                )
                            }
                        }
                        is Result.Error -> {
                            _uiState.update { state ->
                                state.copy(
                                    isSearching = false,
                                    error = result.throwable.message
                                )
                            }
                        }
                        Result.Loading -> {
                            // Already set isSearching = true above
                        }
                    }
                }
            }
        }
    }
}
