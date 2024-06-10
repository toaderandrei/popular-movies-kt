package com.ant.app.ui.main.tvseries

import androidx.lifecycle.viewModelScope
import com.ant.app.ui.base.BaseViewModel
import com.ant.app.ui.extensions.parseResponse
import com.ant.domain.usecases.tvseries.TvShowListUseCase
import com.ant.models.model.TvShowListState
import com.ant.models.request.RequestType
import com.ant.models.request.TvShowType
import com.ant.models.source.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
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
            loadTvSeriesListUseCase(
                Repository.Params(
                    RequestType.TvShowRequest(TvShowType.TOP_RATED),
                    1,
                )
            ).collectAndSetState {
                parseResponse(it, TvShowType.TOP_RATED)
            }
        }

        viewModelScope.launch {
            loadTvSeriesListUseCase(
                Repository.Params(
                    RequestType.TvShowRequest(TvShowType.ONTV_NOW),
                    1,
                )
            ).collectAndSetState {
                parseResponse(it, TvShowType.ONTV_NOW)
            }
        }

        viewModelScope.launch {
            loadTvSeriesListUseCase(
                Repository.Params(
                    RequestType.TvShowRequest(TvShowType.AIRING_TODAY),
                    1,
                )
            ).collectAndSetState {
                parseResponse(it, TvShowType.AIRING_TODAY)
            }
        }

        viewModelScope.launch {
            loadTvSeriesListUseCase(
                Repository.Params(
                    RequestType.TvShowRequest(TvShowType.POPULAR),
                    1,
                )
            ).collectAndSetState {
                parseResponse(it, TvShowType.POPULAR)
            }
        }
    }
}