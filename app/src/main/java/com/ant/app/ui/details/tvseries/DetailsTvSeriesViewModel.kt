package com.ant.app.ui.details.tvseries

import androidx.lifecycle.viewModelScope
import com.ant.app.ui.base.BaseViewModel
import com.ant.common.logger.TmdbLogger
import com.ant.models.entities.TvShowDetails
import com.ant.models.model.Result
import com.ant.models.model.TvSeriesDetailsState
import com.ant.models.model.isLoading
import com.ant.models.model.isSuccess
import com.ant.models.request.RequestType
import com.ant.models.request.TvSeriesAppendToResponseItem
import com.ant.domain.usecases.tvseries.DeleteTvSeriesDetailsUseCase
import com.ant.domain.usecases.tvseries.SaveTvSeriesDetailsUseCase
import com.ant.domain.usecases.tvseries.TvSeriesDetailsUseCase
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
) : BaseViewModel<TvSeriesDetailsState>(TvSeriesDetailsState()) {
    fun loadMovieDetails(movieId: Long) {
        viewModelScope.launch {
            tvSeriesDetailsUseCase(
                Repository.Params(
                    RequestType.TvSeriesRequestDetails(
                        tmdbTvSeriesId = movieId,
                        appendToResponseItems = mutableListOf(
                            TvSeriesAppendToResponseItem.REVIEWS,
                            TvSeriesAppendToResponseItem.VIDEOS,
                            TvSeriesAppendToResponseItem.CREDITS,
                        )
                    )
                )
            ).collectAndSetState {
                logger.d("state: $this")
                parseResponse(it)
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

    private fun TvSeriesDetailsState.parseResponse(it: Result<TvShowDetails>) = if (it.isLoading) {
        copy(loading = true, tvSeriesDetails = null, error = null)
    } else if (it.isSuccess) {
        copy(loading = false, tvSeriesDetails = it.get(), error = null)
    } else {
        copy(loading = false, tvSeriesDetails = null, error = (it as Result.Error).throwable)
    }
}