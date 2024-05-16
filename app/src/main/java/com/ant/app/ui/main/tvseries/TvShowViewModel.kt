package com.ant.app.ui.main.tvseries

import androidx.lifecycle.viewModelScope
import com.ant.app.ui.base.BaseViewModel
import com.ant.domain.usecases.tvseries.TvShowListUseCase
import com.ant.models.model.getErrorOrNull
import com.ant.models.model.isLoading
import com.ant.models.model.isSuccess
import com.ant.models.request.RequestType
import com.ant.models.request.TvShowType
import com.ant.models.source.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvShowViewModel @Inject constructor(
    private val loadTvSeriesListUseCase: TvShowListUseCase,
) : BaseViewModel<TvShowState>(
    TvShowState()
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
                when {
                    it.isLoading -> {
                        copy(
                            isTopRatedTvSeriesLoading = true,
                        )
                    }
                    it.isSuccess -> {
                        copy(
                            topRatedTvSeriesItems = it.get() ?: emptyList(),
                        )
                    }
                    else -> {
                        copy(
                            isTopRatedTvSeriesLoading = false,
                            isTopRatedTvSeriesError = true,
                            topRatedTvSeriesError = it.getErrorOrNull()
                        )
                    }
                }
            }
        }

        viewModelScope.launch {
            loadTvSeriesListUseCase(
                Repository.Params(
                    RequestType.TvShowRequest(TvShowType.ONTV_NOW),
                    1,
                )
            ).collectAndSetState {
                when {
                    it.isLoading -> {
                        copy(
                            isOnTvNowTvSeriesItemsLoading = true,
                        )
                    }
                    it.isSuccess -> {
                        copy(
                            onTvNowTvSeriesItems = it.get() ?: emptyList(),
                        )
                    }
                    else -> {
                        copy(
                            isOnTvNowTvSeriesItemsLoading = false,
                            isOnTvNowTvSeriesError = true,
                            onTvNowTvSeriesError = it.getErrorOrNull()
                        )
                    }
                }
            }
        }

        viewModelScope.launch {
            loadTvSeriesListUseCase(
                Repository.Params(
                    RequestType.TvShowRequest(TvShowType.AIRING_TODAY),
                    1,
                )
            ).collectAndSetState {
                when {
                    it.isLoading -> {
                        copy(
                            isAiringTodayTvSeriesLoading = true,
                        )
                    }
                    it.isSuccess -> {
                        copy(
                            onAiringTodayTvSeriesItems = it.get() ?: emptyList(),
                        )
                    }
                    else -> {
                        copy(
                            isAiringTodayTvSeriesLoading = false,
                            isAiringTodayTvSeriesError = true,
                            airingtodayTvSeriesError = it.getErrorOrNull()
                        )
                    }
                }
            }
        }

        viewModelScope.launch {
            loadTvSeriesListUseCase(
                Repository.Params(
                    RequestType.TvShowRequest(TvShowType.POPULAR),
                    1,
                )
            ).collectAndSetState {
                when {
                    it.isLoading -> {
                        copy(
                            isPopularTvSeriesLoading = true,
                        )
                    }
                    it.isSuccess -> {
                        copy(
                            popularTvSeriesItems = it.get() ?: emptyList(),
                        )
                    }
                    else -> {
                        copy(
                            isPopularTvSeriesLoading = false,
                            isPopularTvSeriesError = true,
                            popularTvSeriesError = it.getErrorOrNull()
                        )
                    }
                }
            }
        }
    }
}