package com.ant.app.ui.details.tvseries

import androidx.lifecycle.viewModelScope
import com.ant.app.ui.base.BaseViewModel
import com.ant.app.ui.extensions.parseResponse
import com.ant.common.logger.TmdbLogger
import com.ant.domain.usecases.tvseries.DeleteTvSeriesDetailsUseCase
import com.ant.domain.usecases.tvseries.SaveTvSeriesDetailsUseCase
import com.ant.domain.usecases.tvseries.TvSeriesDetailsUseCase
import com.ant.models.entities.TvShowDetails
import com.ant.models.model.Error
import com.ant.models.model.Loading
import com.ant.models.model.MoviesState
import com.ant.models.model.Success
import com.ant.models.request.RequestType
import com.ant.models.request.TvSeriesAppendToResponseItem
import com.ant.models.source.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsTvSeriesViewModel @Inject constructor(
    val logger: TmdbLogger,
    val tvSeriesDetailsUseCase: TvSeriesDetailsUseCase,
    val tvSeriesSaveDetailsUseCase: SaveTvSeriesDetailsUseCase,
    val tvSeriesDeleteDetailsUseCase: DeleteTvSeriesDetailsUseCase,
) : BaseViewModel<MoviesState<TvShowDetails>>(MoviesState()) {

    fun loadMovieDetails(movieId: Long) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            try {
                tvSeriesDetailsUseCase.invoke(
                    getTvSeriesRequestDetailsParams(movieId)
                ).collectAndSetState { it ->
                    logger.d("state: $this")
                    parseResponse(response = it,
                        onSuccess = {
                            Success
                        },
                        onLoading = {
                            Loading
                        }
                    ) {
                        logger.e(it, "Error: ${it.message}")
                        Error
                    }
                }
            } catch (
                e: Exception
            ) {
                logger.e(e, "Canceled job: ${e.message}")
            }
        }
    }

    fun retry(movieId: Long) {
        loadMovieDetails(movieId)
    }

    fun saveToDatabase(item: TvShowDetails) {
        viewModelScope.launch {
            tvSeriesSaveDetailsUseCase(item)
                .collect {
                    logger.d("Saved movie details to db.")
                }
        }
    }

    fun deleteFromDatabase(item: TvShowDetails) {
        viewModelScope.launch {
            tvSeriesDeleteDetailsUseCase(item)
                .collect {
                    logger.d("Delete movie details from db.")
                }
        }
    }

    private fun getTvSeriesRequestDetailsParams(movieId: Long) = Repository.Params(
        RequestType.TvSeriesRequestDetails(
            tmdbTvSeriesId = movieId,
            appendToResponseItems = mutableListOf(
                TvSeriesAppendToResponseItem.REVIEWS,
                TvSeriesAppendToResponseItem.VIDEOS,
                TvSeriesAppendToResponseItem.CREDITS,
            )
        )
    )
}