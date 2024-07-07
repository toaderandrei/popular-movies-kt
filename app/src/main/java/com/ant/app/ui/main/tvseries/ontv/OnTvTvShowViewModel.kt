package com.ant.app.ui.main.tvseries.ontv

import com.ant.app.ui.extensions.parseResponse
import com.ant.app.ui.main.base.BaseViewModelMoviesList
import com.ant.common.logger.TmdbLogger
import com.ant.domain.usecases.tvseries.TvShowListUseCase
import com.ant.models.entities.TvShow
import com.ant.models.model.MoviesState
import com.ant.models.model.Result
import com.ant.models.request.RequestType
import com.ant.models.request.TvShowType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnTvTvShowViewModel @Inject constructor(
    tvSeriesListUseCase: TvShowListUseCase,
    tmdbLogger: TmdbLogger
) : BaseViewModelMoviesList<RequestType.TvShowRequest, List<TvShow>>(tmdbLogger, tvSeriesListUseCase) {

    override fun MoviesState<List<TvShow>>.parseDataResponse(it: Result<List<TvShow>>): MoviesState<List<TvShow>> {
        return parseResponse(it)
    }

    override fun getRequest(): RequestType.TvShowRequest {
        return RequestType.TvShowRequest(TvShowType.ONTV_NOW)
    }
}