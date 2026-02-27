package com.ant.network.datasource.tvseries

import com.ant.common.exceptions.bodyOrThrow
import com.ant.common.exceptions.withRetry
import com.ant.models.entities.TvShow
import com.ant.models.request.RequestType
import com.ant.models.request.TvShowType
import com.ant.network.mappers.tvseries.TvSeriesMapper
import com.uwetrottmann.tmdb2.Tmdb
import com.uwetrottmann.tmdb2.entities.TvShowResultsPage
import com.uwetrottmann.tmdb2.services.TvService
import retrofit2.Call
import retrofit2.awaitResponse

class TvSeriesListDataSource(
    private val params: RequestType.TvShowRequest,
    private val tmdb: Tmdb,
    private val tvSeriesMapper: TvSeriesMapper,
) {
    suspend operator fun invoke(): List<TvShow> {
        val tvService = tmdb.tvService()
        val tvShowResultsPageResponse = when (params.tvSeriesType) {
            TvShowType.ONTV_NOW -> onTheAir(tvService, params)
            TvShowType.POPULAR -> popular(tvService, params)
            TvShowType.TOP_RATED -> topRated(tvService, params)
            TvShowType.AIRING_TODAY -> airingToday(tvService, params)
        }.awaitResponse()

        val genreResponse = tmdb.genreService().tv(null)
            .awaitResponse()

        val genresList = genreResponse.bodyOrThrow()
        val tvShowResultsPage = tvShowResultsPageResponse.bodyOrThrow()

        val tvShowList = tvShowResultsPageResponse.let {
            tvSeriesMapper.map(
                Pair(tvShowResultsPage, genresList)
            )
        }
        return tvShowList
    }

    private suspend fun airingToday(
        tvService: TvService,
        params: RequestType.TvShowRequest
    ): Call<TvShowResultsPage> {
        return withRetry {
            tvService.airingToday(params.page, null)
        }
    }


    private suspend fun onTheAir(
        tvService: TvService,
        params: RequestType.TvShowRequest
    ): Call<TvShowResultsPage> {
        return withRetry {
            tvService.onTheAir(params.page, null)
        }
    }

    private suspend fun popular(
        tvService: TvService,
        params: RequestType.TvShowRequest
    ): Call<TvShowResultsPage> {
        return withRetry {
            tvService.popular(params.page, null)
        }
    }


    private suspend fun topRated(
        tvService: TvService,
        params: RequestType.TvShowRequest
    ): Call<TvShowResultsPage> {
        return withRetry {
            tvService.topRated(params.page, null)
        }
    }
}