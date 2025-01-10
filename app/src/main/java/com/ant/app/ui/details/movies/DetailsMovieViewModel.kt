package com.ant.app.ui.details.movies

import androidx.lifecycle.viewModelScope
import com.ant.analytics.AnalyticsEvent
import com.ant.analytics.AnalyticsHelper
import com.ant.analytics.CrashlyticsHelper
import com.ant.ui.viewmodels.BaseViewModel
import com.ant.app.ui.extensions.parseResponse
import com.ant.common.logger.TmdbLogger
import com.ant.domain.usecases.movies.DeleteMovieDetailsUseCase
import com.ant.domain.usecases.movies.MovieDetailsUseCase
import com.ant.domain.usecases.movies.SaveMovieDetailsUseCase
import com.ant.models.entities.MovieDetails
import com.ant.models.model.UIState
import com.ant.models.request.MovieAppendToResponseItem
import com.ant.models.request.RequestType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import logAnalytics
import javax.inject.Inject

@HiltViewModel
class DetailsMovieViewModel @Inject constructor(
    val logger: TmdbLogger,
    val movieDetailsUseCase: MovieDetailsUseCase,
    val movieSaveDetailsUseCase: SaveMovieDetailsUseCase,
    val movieDeleteDetailsUseCase: DeleteMovieDetailsUseCase,
    private val analyticsHelper: AnalyticsHelper,
    private val crashlyticsHelper: CrashlyticsHelper,
) : BaseViewModel<UIState<MovieDetails>>(UIState()) {
    fun loadMovieDetails(movieId: Long) {
        viewModelScope.launch {
            movieDetailsUseCase(
                RequestType.MovieRequestDetails(
                    tmdbMovieId = movieId, appendToResponseItems = mutableListOf(
                        MovieAppendToResponseItem.REVIEWS,
                        MovieAppendToResponseItem.VIDEOS,
                        MovieAppendToResponseItem.CREDITS,
                    )

                )
            ).collectAndSetState {
                logger.d("state: $this")
                parseResponse(response = it,
                    onSuccess = { sucess ->
                        analyticsHelper.logAnalytics(
                            type = AnalyticsEvent.Types.MOVIES_DETAILS_SCREEN,
                            key = AnalyticsEvent.ParamKeys.MOVIES_NAME,
                            value = sucess?.movieData?.name
                        )
                    },
                    onError = { error ->
                        crashlyticsHelper.logError(error)
                    })
            }
        }
    }

    fun retry(movieId: Long) {
        loadMovieDetails(movieId)
    }

    fun saveAsFavorite(item: MovieDetails) {
        viewModelScope.launch {
            movieSaveDetailsUseCase(item).collect {
                logger.d("Saved movie details to db.")
            }
        }
    }

    fun deleteFavorite(item: MovieDetails) {
        viewModelScope.launch {
            movieDeleteDetailsUseCase(item).collect {
                logger.d("Delete movie details from db.")
            }
        }
    }
}