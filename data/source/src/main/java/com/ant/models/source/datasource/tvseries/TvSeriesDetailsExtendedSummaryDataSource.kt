package com.ant.models.source.datasource.tvseries


import com.ant.models.entities.TvShowDetails
import com.ant.models.request.RequestType
import com.ant.models.request.toAppendRequest
import com.ant.models.request.toMovieId
import com.ant.models.source.extensions.bodyOrThrow
import com.ant.models.source.extensions.withRetry
import com.ant.models.source.mappers.tvseries.TvSeriesDetailsMapper
import com.ant.models.source.repositories.Repository
import com.uwetrottmann.tmdb2.Tmdb
import retrofit2.awaitResponse

class TvSeriesDetailsExtendedSummaryDataSource(
    private val params: Repository.Params<RequestType.TvSeriesRequestDetails>,
    private val tmdb: Tmdb,
    private val tvSeriesDetailsMapper: TvSeriesDetailsMapper
) {
    suspend operator fun invoke(): TvShowDetails {
        val tvService = tmdb.tvService()
        return withRetry {
            tvService.tv(params.request.toMovieId(), null, params.request.toAppendRequest())
        }.awaitResponse().let {
            tvSeriesDetailsMapper.map(
                it.bodyOrThrow()
            )
        }
    }
}