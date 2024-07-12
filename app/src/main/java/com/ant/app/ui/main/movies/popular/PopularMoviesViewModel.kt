package com.ant.app.ui.main.movies.popular

import com.ant.app.ui.main.base.BaseViewModelMoviesList
import com.ant.common.logger.TmdbLogger
import com.ant.domain.usecases.movies.MovieListUseCase
import com.ant.models.entities.MovieData
import com.ant.models.request.MovieType
import com.ant.models.request.RequestType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PopularMoviesViewModel @Inject constructor(
    movieListUseCase: MovieListUseCase,
    tmdbLogger: TmdbLogger
) : BaseViewModelMoviesList<RequestType.MovieRequest, MovieData>(
    tmdbLogger,
    movieListUseCase
) {

    override fun getRequest(): RequestType.MovieRequest {
        return RequestType.MovieRequest(MovieType.POPULAR)
    }
}