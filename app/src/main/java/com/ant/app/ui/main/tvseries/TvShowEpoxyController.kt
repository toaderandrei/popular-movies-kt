package com.ant.app.ui.main.tvseries

import android.annotation.SuppressLint
import com.airbnb.epoxy.EpoxyController
import com.ant.resources.R as R2
import com.ant.common.logger.TmdbLogger
import com.ant.models.entities.ImageEntity
import com.ant.models.entities.TvShow
import com.ant.models.extensions.toReadableError
import com.ant.epoxy.extensions.tmdbCarousel
import com.ant.epoxy.extensions.withModelsFrom
import com.ant.layouts.*
import com.ant.models.model.TvShowListState
import com.ant.models.model.isError
import com.ant.ui.extensions.observable
import com.ant.tmdb.old.PosterSizes
import javax.inject.Inject

class TvShowEpoxyController @Inject constructor(
    private val logger: TmdbLogger,
) : EpoxyController() {
    var callbacks: Callbacks? by observable(null, ::requestModelBuild)
    var state: TvShowListState by observable(TvShowListState(), ::requestModelBuild)

    interface Callbacks {
        fun onPopularTvSeriesClicked()
        fun onTopRatedTvSeriesClicked()
        fun onAiringTodayTvSeriesClicked()
        fun onTvNowTvSeriesClicked()
        fun onItemClicked(viewHolderId: Long, item: TvShow)
    }

    @SuppressLint("StringFormatInvalid")
    override fun buildModels() {
        val popular = state.popularTvSeries.data
        val top = state.topRated.data
        val nowOnTv = state.onTvNow.data
        val onAiringToday = state.airingToday.data
        logger.d("building tv series models")

        // todo use the strings value instead of hardcoded.
        buildModel(
            popular,
            "popular_header",
            movieTitle = R2.string.tvshows_popular,
            "popular_carousel",
            "popular_placeholder",
            errorMessage = this@TvShowEpoxyController.state.popularTvSeries.error?.toReadableError(),
            isLoading = this@TvShowEpoxyController.state.popularTvSeries.loading,
            isError = this@TvShowEpoxyController.state.popularTvSeries.isError,
            callback = { callbacks?.onPopularTvSeriesClicked() }
        )

        // todo use the strings value instead of hardcoded.
        buildModel(
            top,
            "top_rated_header",
            movieTitle = R2.string.tvshows_toprated,
            "top_rated_carousel",
            "top_rated_placeholder",
            errorMessage = this@TvShowEpoxyController.state.topRated.error?.toReadableError(),
            isLoading = this@TvShowEpoxyController.state.topRated.loading,
            isError = this@TvShowEpoxyController.state.topRated.isError,
            callback = { callbacks?.onTopRatedTvSeriesClicked() }
        )

        // todo use the strings value instead of hardcoded.
        buildModel(
            nowOnTv,
            "now_on_tv_header",
            movieTitle = R2.string.tvshows_now_on_tv,
            "now_on_tv_carousel",
            "now_on_tv_placeholder",
            errorMessage = this@TvShowEpoxyController.state.onTvNow.error?.toReadableError(),
            isLoading = this@TvShowEpoxyController.state.onTvNow.loading,
            isError = this@TvShowEpoxyController.state.onTvNow.isError,
            callback = { callbacks?.onTvNowTvSeriesClicked() }
        )

        // todo use the strings value instead of hardcoded.
        buildModel(
            onAiringToday,
            "airing_today",
            movieTitle = R2.string.tvshows_airing_today,
            "airing_today_carousel",
            "airing_today_placeholder",
            errorMessage = this@TvShowEpoxyController.state.airingToday.error?.toReadableError(),
            isLoading = this@TvShowEpoxyController.state.airingToday.loading,
            isError = this@TvShowEpoxyController.state.airingToday.isError,
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

