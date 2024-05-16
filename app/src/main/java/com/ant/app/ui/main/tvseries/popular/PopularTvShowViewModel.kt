package com.ant.app.ui.main.tvseries.popular

import com.ant.app.ui.main.base.tvseries.BaseViewModelTvShowList
import com.ant.common.logger.TmdbLogger
import com.ant.models.request.RequestType
import com.ant.models.request.TvShowType
import com.ant.domain.usecases.tvseries.TvShowListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PopularTvShowViewModel @Inject constructor(
    tvSeriesListUseCase: TvShowListUseCase,
    tmdbLogger: TmdbLogger
) : BaseViewModelTvShowList(tmdbLogger, tvSeriesListUseCase) {

    override fun getTvSeriesRequest(): RequestType.TvShowRequest {
        return RequestType.TvShowRequest(TvShowType.POPULAR)
    }
}