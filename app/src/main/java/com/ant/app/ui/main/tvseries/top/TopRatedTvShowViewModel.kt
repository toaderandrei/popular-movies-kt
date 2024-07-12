package com.ant.app.ui.main.tvseries.top

import com.ant.app.ui.main.base.BaseViewModelMoviesList
import com.ant.common.logger.TmdbLogger
import com.ant.domain.usecases.tvseries.TvShowListUseCase
import com.ant.models.entities.TvShow
import com.ant.models.request.RequestType
import com.ant.models.request.TvShowType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TopRatedTvShowViewModel @Inject constructor(
    tvSeriesListUseCase: TvShowListUseCase,
    val tmdbLogger: TmdbLogger
) : BaseViewModelMoviesList<RequestType.TvShowRequest, TvShow>(
    tmdbLogger,
    tvSeriesListUseCase
) {

    override fun getRequest(): RequestType.TvShowRequest {
        return RequestType.TvShowRequest(TvShowType.TOP_RATED)
    }
}