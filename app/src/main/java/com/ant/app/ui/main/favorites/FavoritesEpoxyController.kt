package com.ant.app.ui.main.favorites

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.epoxy.EpoxyController
import com.ant.common.logger.TmdbLogger
import com.ant.epoxy.extensions.tmdbCarousel
import com.ant.epoxy.extensions.withModelsFrom
import com.ant.layouts.MovieItemBindingModel_
import com.ant.layouts.TvseriesItemBindingModel_
import com.ant.layouts.errorState
import com.ant.layouts.header
import com.ant.layouts.infiniteLoading
import com.ant.models.entities.ImageEntity
import com.ant.models.entities.MovieData
import com.ant.models.entities.TvShow
import com.ant.models.model.FavoritesState
import com.ant.models.source.extensions.observable
import com.ant.tmdb.old.PosterSizes
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject
import com.ant.resources.R as R2

class FavoritesEpoxyController @Inject constructor(
    @ActivityContext private val context: Context,
    private val logger: TmdbLogger,
) : EpoxyController() {
    var callbacks: Callbacks? by observable(null, ::requestModelBuild)
    var state: FavoritesState by observable(FavoritesState(), ::requestModelBuild)

    interface Callbacks {
        fun onTvSeriesItemClicked(viewHolderId: Long, item: TvShow)
        fun onMoviesItemClicked(viewHolderId: Long, item: MovieData)
    }

    @SuppressLint("StringFormatInvalid")
    override fun buildModels() {
        val moviesFavoredResult = state.moviesFavored.data
        val tvShowFavoredResult = state.tvSeriesFavored.data
        logger.d("building models")

        // todo use the strings value instead of hardcoded.
        header {
            id("movies_favored_header")
            title(R2.string.movies_favored)
        }

        if (!moviesFavoredResult.isNullOrEmpty()) {
            logger.d("building model: movie")
            tmdbCarousel {
                id("movies_favored_carousel")
                hasFixedSize(true)

                withModelsFrom(moviesFavoredResult) { item ->
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
                            this@FavoritesEpoxyController.callbacks?.onMoviesItemClicked(
                                model.id(),
                                item
                            )
                        }
                    }
                }
            }
        } else if (state.moviesFavored.loading) {
            infiniteLoading {
                id("loading_carousel")
            }
        } else {
            errorState {
                id("empty_carousel_movies")
                message(
                    this@FavoritesEpoxyController.context.getString(
                        R2.string.empty_results,
                        "Favored Movies"
                    ),
                )
            }
        }


        header {
            id("tvseries_favored_header")
            title(R2.string.tvshows_favored)
        }

        if (!tvShowFavoredResult.isNullOrEmpty()) {
            logger.d("building model: movie")
            tmdbCarousel {
                id("tvseries_favored_carousel")
                hasFixedSize(true)

                withModelsFrom(tvShowFavoredResult) { item ->
                    TvseriesItemBindingModel_().apply {
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
                            this@FavoritesEpoxyController.callbacks?.onTvSeriesItemClicked(
                                model.id(),
                                item
                            )
                        }
                    }
                }
            }
        } else if (state.tvSeriesFavored.loading) {
            infiniteLoading {
                id("loading_carousel")
            }
        } else {
            errorState {
                id("empty_carousel_tvseries")
                message(
                    this@FavoritesEpoxyController.context.getString(
                        R2.string.empty_results,
                        "Favored TvSeries"
                    ),
                )
            }
        }
    }
}

