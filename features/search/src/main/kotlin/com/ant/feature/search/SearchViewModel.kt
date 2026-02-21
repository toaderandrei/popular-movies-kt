package com.ant.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ant.domain.usecases.search.SearchMovieUseCase
import com.ant.domain.usecases.search.SearchTvShowUseCase
import com.ant.models.model.Result
import com.ant.models.request.RequestType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
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
    private val searchMovieUseCase: SearchMovieUseCase,
    private val searchTvShowUseCase: SearchTvShowUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    private var searchJob: Job? = null

    init {
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
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _uiState.update { it.copy(isSearching = true, error = null) }

            var moviesComplete = false
            var tvShowsComplete = false

            // Search movies
            launch {
                searchMovieUseCase(RequestType.SearchMovieRequest(query)).collect { result ->
                    when (result) {
                        is Result.Success -> {
                            moviesComplete = true
                            _uiState.update { state ->
                                state.copy(
                                    movieResults = result.data,
                                    isSearching = !tvShowsComplete,
                                )
                            }
                        }
                        is Result.Error -> {
                            moviesComplete = true
                            _uiState.update { state ->
                                state.copy(
                                    isSearching = !tvShowsComplete,
                                    error = result.throwable.message,
                                )
                            }
                        }
                        Result.Loading -> { /* already set above */ }
                    }
                }
            }

            // Search TV shows
            launch {
                searchTvShowUseCase(RequestType.SearchTvShowRequest(query)).collect { result ->
                    when (result) {
                        is Result.Success -> {
                            tvShowsComplete = true
                            _uiState.update { state ->
                                state.copy(
                                    tvShowResults = result.data,
                                    isSearching = !moviesComplete,
                                )
                            }
                        }
                        is Result.Error -> {
                            tvShowsComplete = true
                            _uiState.update { state ->
                                state.copy(
                                    isSearching = !moviesComplete,
                                    error = result.throwable.message,
                                )
                            }
                        }
                        Result.Loading -> { /* already set above */ }
                    }
                }
            }
        }
    }
}
