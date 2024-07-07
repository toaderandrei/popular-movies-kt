package com.ant.app.ui.main.movies.upcoming

import com.ant.app.ui.extensions.parseResponse
import com.ant.app.ui.main.base.BaseViewModelMoviesList
import com.ant.common.logger.TmdbLogger
import com.ant.domain.usecases.movies.MovieListUseCase
import com.ant.models.entities.MovieData
import com.ant.models.model.MoviesState
import com.ant.models.model.Result
import com.ant.models.request.MovieType
import com.ant.models.request.RequestType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UpcomingMoviesViewModel @Inject constructor(
    movieListUseCase: MovieListUseCase,
    tmdbLogger: TmdbLogger
) : BaseViewModelMoviesList<RequestType.MovieRequest, List<MovieData>>(
    tmdbLogger,
    movieListUseCase
) {
    override fun MoviesState<List<MovieData>>.parseDataResponse(it: Result<List<MovieData>>): MoviesState<List<MovieData>> {
        return parseResponse(it)
    }

    override fun getRequest(): RequestType.MovieRequest {
        return RequestType.MovieRequest(MovieType.UPCOMING)
    }
}