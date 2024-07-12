package com.ant.app.ui.main.tvseries.ontv

import com.ant.app.ui.main.base.BaseViewModelMoviesList
import com.ant.common.logger.TmdbLogger
import com.ant.domain.usecases.tvseries.TvShowListUseCase
import com.ant.models.entities.TvShow
import com.ant.models.request.RequestType
import com.ant.models.request.TvShowType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnTvTvShowViewModel @Inject constructor(
    tvSeriesListUseCase: TvShowListUseCase,
    tmdbLogger: TmdbLogger
) : BaseViewModelMoviesList<RequestType.TvShowRequest, TvShow>(
    tmdbLogger,
    tvSeriesListUseCase
) {

    override fun getRequest(): RequestType.TvShowRequest {
        return RequestType.TvShowRequest(TvShowType.ONTV_NOW)
    }
}