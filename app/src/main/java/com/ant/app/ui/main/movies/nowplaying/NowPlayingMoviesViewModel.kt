package com.ant.app.ui.main.movies.nowplaying

import com.ant.app.ui.main.base.movies.BaseViewModelMovieList
import com.ant.common.logger.TmdbLogger
import com.ant.domain.usecases.movies.MovieListUseCase
import com.ant.models.request.MovieType
import com.ant.models.request.RequestType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NowPlayingMoviesViewModel @Inject constructor(
    movieListUseCase: MovieListUseCase, val tmdbLogger: TmdbLogger
) : BaseViewModelMovieList(tmdbLogger, movieListUseCase) {

    override fun getMovieRequest(): RequestType.MovieRequest {
        return RequestType.MovieRequest(MovieType.NOW_PLAYING)
    }
}