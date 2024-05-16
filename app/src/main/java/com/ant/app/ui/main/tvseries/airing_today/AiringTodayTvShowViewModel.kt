package com.ant.app.ui.main.tvseries.airing_today

import com.ant.app.ui.main.base.tvseries.BaseViewModelTvShowList
import com.ant.common.logger.TmdbLogger
import com.ant.domain.usecases.tvseries.TvShowListUseCase
import com.ant.models.request.RequestType
import com.ant.models.request.TvShowType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AiringTodayTvShowViewModel @Inject constructor(
    useCase: TvShowListUseCase,
    tmdbLogger: TmdbLogger
) : BaseViewModelTvShowList(tmdbLogger, useCase) {

    override fun getTvSeriesRequest(): RequestType.TvShowRequest {
        return RequestType.TvShowRequest(TvShowType.AIRING_TODAY)
    }
}