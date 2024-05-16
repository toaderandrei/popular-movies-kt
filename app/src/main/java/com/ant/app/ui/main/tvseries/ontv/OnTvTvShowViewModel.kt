package com.ant.app.ui.main.tvseries.ontv

import com.ant.app.ui.main.base.tvseries.BaseViewModelTvShowList
import com.ant.common.logger.TmdbLogger
import com.ant.domain.usecases.tvseries.TvShowListUseCase
import com.ant.models.request.RequestType
import com.ant.models.request.TvShowType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnTvTvShowViewModel @Inject constructor(
    tvSeriesListUseCase: TvShowListUseCase,
    tmdbLogger: TmdbLogger
) : BaseViewModelTvShowList(tmdbLogger, tvSeriesListUseCase) {

    override fun getTvSeriesRequest(): RequestType.TvShowRequest {
        return RequestType.TvShowRequest(TvShowType.ONTV_NOW)
    }
}