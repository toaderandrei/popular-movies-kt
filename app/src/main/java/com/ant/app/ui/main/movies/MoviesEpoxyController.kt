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
    var state: MoviesState by observable(MoviesState(), ::requestModelBuild)

    interface Callbacks {
        fun onPopularMoviesClicked()
        fun onTopMoviesClicked()
        fun onNowPlayingMoviesClicked()
        fun onUpComingMoviesClicked()
        fun onItemClicked(viewHolderId: Long, item: MovieData)
    }

    @SuppressLint("StringFormatInvalid")
    override fun buildModels() {
        val popular = state.popularItems
        val top = state.topMovieItems
        val nowPlaying = state.nowPlayingItems
        val onUpcoming = state.upcomingItems
        logger.d("building models")

        // todo use the strings value instead of hardcoded.
        buildModel(
            popular,
            "popular_header",
            movieTitle = R2.string.movies_popular,
            "popular_carousel",
            "popular_placeholder",
            errorMessage = this@MoviesEpoxyController.state.popularMoviesError?.toReadableError(),
            isLoading = this@MoviesEpoxyController.state.isPopularMoviesLoading,
            isError = this@MoviesEpoxyController.state.isPopularMoviesError,
            callback = { callbacks?.onPopularMoviesClicked() }
        )

        // todo use the strings value instead of hardcoded.
        buildModel(
            top,
            "top_header",
            movieTitle = R2.string.movies_toprated,
            "top_carousel",
            "top_placeholder",
            errorMessage = this@MoviesEpoxyController.state.topMoviesError?.toReadableError(),
            isLoading = this@MoviesEpoxyController.state.isTopMoviesLoading,
            isError = this@MoviesEpoxyController.state.isTopMoviesError,
            callback = { callbacks?.onTopMoviesClicked() }
        )

        // todo use the strings value instead of hardcoded.
        buildModel(
            nowPlaying,
            "now_playing_header",
            movieTitle = R2.string.movies_now_on_theaters,
            "now_playing_carousel",
            "now_playing_placeholder",
            errorMessage = this@MoviesEpoxyController.state.nowPlayingMoviesError?.toReadableError(),
            isLoading = this@MoviesEpoxyController.state.isNowPlayingMoviesLoading,
            isError = this@MoviesEpoxyController.state.isNowPlayingMoviesError,
            callback = { callbacks?.onNowPlayingMoviesClicked() }
        )

        // todo use the strings value instead of hardcoded.
        buildModel(
            onUpcoming,
            "upcoming_header",
            movieTitle = R2.string.movies_upcoming,
            "upcoming_carousel",
            "upcoming_placeholder",
            errorMessage = this@MoviesEpoxyController.state.upcomingMoviesError?.toReadableError(),
            isLoading = this@MoviesEpoxyController.state.isUpcomingMoviesLoading,
            isError = this@MoviesEpoxyController.state.isUpcomingMoviesError,
            callback = { callbacks?.onUpComingMoviesClicked() }
        )
    }

    private fun buildModel(
        moviesResult: List<MovieData> = emptyList(),
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

        when {
            moviesResult.isNotEmpty() -> {
                logger.d("building model: movie")
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

            isLoading -> {
                infiniteLoading {
                    id("loading_carousel")
                }
            }

            isError -> {
                errorState {
                    id(emptyCarousel)
                    message(errorMessage)
                }
            }
        }
    }
}

