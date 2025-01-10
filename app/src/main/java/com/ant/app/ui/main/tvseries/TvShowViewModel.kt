package com.ant.app.ui.main.tvseries

import androidx.lifecycle.viewModelScope
import com.ant.app.ui.extensions.parseResponse
import com.ant.domain.usecases.tvseries.TvShowListUseCase
import com.ant.models.model.TvShowListState
import com.ant.models.request.RequestType
import com.ant.models.request.TvShowType
import com.ant.ui.viewmodels.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvShowViewModel @Inject constructor(
    private val loadTvSeriesListUseCase: TvShowListUseCase,
) : BaseViewModel<TvShowListState>(
    TvShowListState()
) {
    init {
        loadAllMovies()
    }

    fun refresh() {
        loadAllMovies()
    }

    private fun loadAllMovies() {
        viewModelScope.launch {

            // Use coroutineScope to ensure all tasks complete before continuing
            coroutineScope {
                // Launch the four requests concurrently using async
                val topRatedTvShowsDeferred = async {
                    loadTvSeriesListUseCase(
                        RequestType.TvShowRequest(TvShowType.TOP_RATED),

                        ).collectAndSetState {
                        parseResponse(it, TvShowType.TOP_RATED)
                    }
                }

                val onTvNowDeferred = async {
                    loadTvSeriesListUseCase(
                        RequestType.TvShowRequest(TvShowType.ONTV_NOW),

                        ).collectAndSetState {
                        parseResponse(it, TvShowType.ONTV_NOW)
                    }
                }

                val airingTodayDeferred = async {
                    loadTvSeriesListUseCase(
                        RequestType.TvShowRequest(TvShowType.AIRING_TODAY),
                    ).collectAndSetState {
                        parseResponse(it, TvShowType.AIRING_TODAY)
                    }
                }

                val popularTvShowsDeferred = async {
                    loadTvSeriesListUseCase(
                        RequestType.TvShowRequest(TvShowType.POPULAR),
                    ).collectAndSetState {
                        parseResponse(it, TvShowType.POPULAR)
                    }
                }

                // Await all the deferred results to ensure they all complete
                topRatedTvShowsDeferred.await()
                onTvNowDeferred.await()
                airingTodayDeferred.await()
                popularTvShowsDeferred.await()
            }
        }
    }
}