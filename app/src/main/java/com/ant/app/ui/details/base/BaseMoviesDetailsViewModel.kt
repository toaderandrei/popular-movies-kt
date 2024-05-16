package com.ant.app.ui.details.base

import com.ant.app.ui.base.BaseViewModel
import com.ant.common.logger.TmdbLogger
import com.ant.domain.usecases.movies.MovieDetailsUseCase

abstract class BaseMoviesDetailsViewModel<S>(
    val logger: TmdbLogger,
    val useCase: MovieDetailsUseCase,
    initialState: S,
) : BaseViewModel<S>(initialState){

}