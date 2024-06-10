package com.ant.app.ui.main.movies

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.epoxy.EpoxyController
import com.ant.common.logger.TmdbLogger
import com.ant.epoxy.extensions.tmdbCarousel
import com.ant.epoxy.extensions.withModelsFrom
import com.ant.layouts.MovieItemBindingModel_
import com.ant.layouts.errorState
import com.ant.layouts.header
import com.ant.layouts.infiniteLoading
import com.ant.models.entities.ImageEntity
import com.ant.models.entities.MovieData
import com.ant.models.extensions.toReadableError
import com.ant.models.model.Error
import com.ant.models.model.MoviesListState
import com.ant.models.model.errorMessage
import com.ant.models.model.isError
import com.ant.models.source.extensions.observable
import com.ant.tmdb.old.PosterSizes
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject
import com.ant.resources.R as R2

class MoviesEpoxyController @Inject constructor(
    @ActivityContext private val context: Context,
    private val logger: TmdbLogger,
) : EpoxyController() {
    var callbacks: Callbacks? by observable(null, ::requestModelBuild)
    var state: MoviesListState by observable(MoviesListState(), ::requestModelBuild)

    interface Callbacks {
        fun onPopularMoviesClicked()
        fun onTopMoviesClicked()
        fun onNowPlayingMoviesClicked()
        fun onUpComingMoviesClicked()
        fun onItemClicked(viewHolderId: Long, item: MovieData)
    }

    @SuppressLint("StringFormatInvalid")
    override fun buildModels() {
        val popular = state.popularMovies.data
        val top = state.topMovies.data
        val nowPlaying = state.nowPlayingMovies.data
        val onUpcoming = state.upcomingMovies.data
        logger.d("building models")

        // todo use the strings value instead of hardcoded.
        buildModel(
            popular,
            "popular_header",
            movieTitle = R2.string.movies_popular,
            "popular_carousel",
            "popular_placeholder",
            errorMessage = this@MoviesEpoxyController.state.popularMovies.error?.toReadableError(),
            isLoading = this@MoviesEpoxyController.state.popularMovies.loading,
            isError = this@MoviesEpoxyController.state.popularMovies.isError,
            callback = { callbacks?.onPopularMoviesClicked() }
        )

        // todo use the strings value instead of hardcoded.
        buildModel(
            top,
            "top_header",
            movieTitle = R2.string.movies_toprated,
            "top_carousel",
            "top_placeholder",
            errorMessage = this@MoviesEpoxyController.state.topMovies.error?.toReadableError(),
            isLoading = this@MoviesEpoxyController.state.topMovies.loading,
            isError = this@MoviesEpoxyController.state.topMovies.isError,
            callback = { callbacks?.onTopMoviesClicked() }
        )

        // todo use the strings value instead of hardcoded.
        buildModel(
            nowPlaying,
            "now_playing_header",
            movieTitle = R2.string.movies_now_on_theaters,
            "now_playing_carousel",
            "now_playing_placeholder",
            errorMessage = this@MoviesEpoxyController.state.nowPlayingMovies.error?.toReadableError(),
            isLoading = this@MoviesEpoxyController.state.nowPlayingMovies.loading,
            isError = this@MoviesEpoxyController.state.nowPlayingMovies.isError,
            callback = { callbacks?.onNowPlayingMoviesClicked() }
        )

        // todo use the strings value instead of hardcoded.
        buildModel(
            onUpcoming,
            "upcoming_header",
            movieTitle = R2.string.movies_upcoming,
            "upcoming_carousel",
            "upcoming_placeholder",
            errorMessage = this@MoviesEpoxyController.state.upcomingMovies.error?.toReadableError(),
            isLoading = this@MoviesEpoxyController.state.upcomingMovies.loading,
            isError = this@MoviesEpoxyController.state.upcomingMovies.isError,
            callback = { callbacks?.onUpComingMoviesClicked() }
        )
    }

    private fun buildModel(
        moviesResult: List<MovieData>? = null,
        keyId: String? = "",
        movieTitle: Int = -1,
        carouselId: String? = "",
        emptyCarousel: String? = "",
        errorMessage: String? = "",
        isLoading: Boolean = false,
        isError: Boolean = false,
        callback: () -> Unit?,
    ) {

        header {
            id(keyId)
            title(movieTitle)
            buttonClickListener { _ -> callback() }
        }
        if (moviesResult.isNullOrEmpty()) {
            logger.d("building model: null. $keyId")
        }

        when {
            !moviesResult.isNullOrEmpty() -> {
                logger.d("building model: $moviesResult")
                tmdbCarousel {
                    id(carouselId)
                    hasFixedSize(true)

                    withModelsFrom(moviesResult) { item ->
                        MovieItemBindingModel_().apply {
                            id(item.id)

                            movie(item)
                            item.posterPath?.let {
                                movieEntity(
                                    ImageEntity(
                                        it,
                                        PosterSizes.W154.wSize
                                    )
                                )
                            }
                            clickListener { model, _, _, _ ->
                                this@MoviesEpoxyController.callbacks?.onItemClicked(
                                    model.id(),
                                    item
                                )
                            }
                        }
                    }
                }
            }


            isError -> {
                errorState {
                    id(emptyCarousel)
                    message(errorMessage)
                }
            }
            isLoading -> {
                infiniteLoading {
                    id("loading_carousel")
                }
            }
            else -> {
                infiniteLoading {
                    id("loading_carousel")
                }
            }
        }
    }
}

