package com.ant.app.ui.main.tvseries.top

import com.ant.app.ui.main.base.tvseries.BaseViewModelTvShowList
import com.ant.common.logger.TmdbLogger
import com.ant.domain.usecases.tvseries.TvShowListUseCase
import com.ant.models.request.RequestType
import com.ant.models.request.TvShowType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TopRatedTvShowViewModel @Inject constructor(
    tvSeriesListUseCase: TvShowListUseCase,
    val tmdbLogger: TmdbLogger
) : BaseViewModelTvShowList(tmdbLogger, tvSeriesListUseCase) {

    override fun getTvSeriesRequest(): RequestType.TvShowRequest {
        return RequestType.TvShowRequest(TvShowType.TOP_RATED)
    }
}