package com.ant.app.ui.main.tvseries

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.epoxy.EpoxyController
import com.ant.app.R
import com.ant.resources.R as R2
import com.ant.common.logger.TmdbLogger
import com.ant.models.entities.ImageEntity
import com.ant.models.entities.TvShow
import com.ant.models.extensions.toReadableError
import com.ant.epoxy.extensions.tmdbCarousel
import com.ant.epoxy.extensions.withModelsFrom
import com.ant.layouts.*
import com.ant.models.source.extensions.observable
import com.ant.tmdb.old.PosterSizes
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class TvShowEpoxyController @Inject constructor(
    @ActivityContext private val context: Context,
    private val logger: TmdbLogger,
) : EpoxyController() {
    var callbacks: Callbacks? by observable(null, ::requestModelBuild)
    var state: TvShowState by observable(TvShowState(), ::requestModelBuild)

    interface Callbacks {
        fun onPopularTvSeriesClicked()
        fun onTopRatedTvSeriesClicked()
        fun onAiringTodayTvSeriesClicked()
        fun onTvNowTvSeriesClicked()
        fun onItemClicked(viewHolderId: Long, item: TvShow)
    }

    @SuppressLint("StringFormatInvalid")
    override fun buildModels() {
        val popular = state.popularTvSeriesItems
        val top = state.topRatedTvSeriesItems
        val nowOnTv = state.onTvNowTvSeriesItems
        val onAiringToday = state.onAiringTodayTvSeriesItems
        logger.d("building tv series models")

        // todo use the strings value instead of hardcoded.
        buildModel(
            popular,
            "popular_header",
            movieTitle = R2.string.tvshows_popular,
            "popular_carousel",
            "popular_placeholder",
            errorMessage = this@TvShowEpoxyController.state.popularTvSeriesError?.toReadableError(),
            isLoading = this@TvShowEpoxyController.state.isPopularTvSeriesLoading,
            isError = this@TvShowEpoxyController.state.isPopularTvSeriesError,
            callback = { callbacks?.onPopularTvSeriesClicked() }
        )

        // todo use the strings value instead of hardcoded.
        buildModel(
            top,
            "top_rated_header",
            movieTitle = R2.string.tvshows_toprated,
            "top_rated_carousel",
            "top_rated_placeholder",
            errorMessage = this@TvShowEpoxyController.state.topRatedTvSeriesError?.toReadableError(),
            isLoading = this@TvShowEpoxyController.state.isTopRatedTvSeriesLoading,
            isError = this@TvShowEpoxyController.state.isTopRatedTvSeriesError,
            callback = { callbacks?.onTopRatedTvSeriesClicked() }
        )

        // todo use the strings value instead of hardcoded.
        buildModel(
            nowOnTv,
            "now_on_tv_header",
            movieTitle = R2.string.tvshows_now_on_tv,
            "now_on_tv_carousel",
            "now_on_tv_placeholder",
            errorMessage = this@TvShowEpoxyController.state.onTvNowTvSeriesError?.toReadableError(),
            isLoading = this@TvShowEpoxyController.state.isOnTvNowTvSeriesItemsLoading,
            isError = this@TvShowEpoxyController.state.isOnTvNowTvSeriesError,
            callback = { callbacks?.onTvNowTvSeriesClicked() }
        )

        // todo use the strings value instead of hardcoded.
        buildModel(
            onAiringToday,
            "airing_today",
            movieTitle = R2.string.tvshows_airing_today,
            "airing_today_carousel",
            "airing_today_placeholder",
            errorMessage = this@TvShowEpoxyController.state.airingtodayTvSeriesError?.toReadableError(),
            isLoading = this@TvShowEpoxyController.state.isAiringTodayTvSeriesLoading,
            isError = this@TvShowEpoxyController.state.isAiringTodayTvSeriesError,
            callback = { callbacks?.onAiringTodayTvSeriesClicked() }
        )
    }

    private fun buildModel(
        moviesResult: List<TvShow>? = null,
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
            !moviesResult.isNullOrEmpty() -> {
                logger.d("building model: tvseries")
                tmdbCarousel {
                    id(carouselId)
                    hasFixedSize(true)

                    withModelsFrom(moviesResult) { item ->
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
                                this@TvShowEpoxyController.callbacks?.onItemClicked(model.id(), item)
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

