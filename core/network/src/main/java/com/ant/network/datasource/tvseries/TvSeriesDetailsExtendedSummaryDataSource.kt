package com.ant.network.datasource.tvseries


import com.ant.common.exceptions.bodyOrThrow
import com.ant.common.exceptions.withRetry
import com.ant.models.entities.TvShowDetails
import com.ant.models.request.RequestType
import com.ant.models.request.toAppendRequest
import com.ant.models.request.toMovieId
import com.ant.network.mappers.tvseries.TvSeriesDetailsMapper
import com.uwetrottmann.tmdb2.Tmdb
import retrofit2.awaitResponse

class TvSeriesDetailsExtendedSummaryDataSource(
    private val params: RequestType.TvSeriesRequestDetails,
    private val tmdb: Tmdb,
    private val tvSeriesDetailsMapper: TvSeriesDetailsMapper
) {
    suspend operator fun invoke(): TvShowDetails {
        val tvService = tmdb.tvService()
        return withRetry {
            tvService.tv(params.toMovieId(), null, params.toAppendRequest())
        }.awaitResponse().let {
            tvSeriesDetailsMapper.map(
                it.bodyOrThrow()
            )
        }
    }
}