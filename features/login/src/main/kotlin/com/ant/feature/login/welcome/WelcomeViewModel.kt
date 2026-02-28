package com.ant.feature.login.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ant.domain.usecases.movies.MovieListUseCase
import com.ant.models.model.Result
import com.ant.models.request.MovieType
import com.ant.models.request.RequestType
import com.ant.models.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TMDB_BACKDROP_BASE_URL = "https://image.tmdb.org/t/p/w1280"

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val movieListUseCase: MovieListUseCase,
    private val sessionManager: SessionManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WelcomeUiState())
    val uiState: StateFlow<WelcomeUiState> = _uiState.asStateFlow()

    init {
        loadBackdropImage()
    }

    private fun loadBackdropImage() {
        viewModelScope.launch {
            val request = RequestType.MovieRequest(
                movieType = MovieType.POPULAR,
                page = 1,
            )
            movieListUseCase(request).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Result.Success -> {
                        val movie = result.data.items
                            .filter { !it.backDropPath.isNullOrBlank() }
                            .randomOrNull()
                        _uiState.update {
                            it.copy(
                                backdropUrl = movie?.backDropPath?.let { path ->
                                    "$TMDB_BACKDROP_BASE_URL$path"
                                },
                                movieTitle = movie?.name,
                                isLoading = false,
                            )
                        }
                    }
                    is Result.Error -> {
                        _uiState.update { it.copy(isLoading = false) }
                    }
                }
            }
        }
    }

    fun continueAsGuest() {
        viewModelScope.launch {
            sessionManager.setGuestMode(true)
            _uiState.update { it.copy(guestModeActivated = true) }
        }
    }
}
