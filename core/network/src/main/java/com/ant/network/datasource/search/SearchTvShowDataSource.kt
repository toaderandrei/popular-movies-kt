package com.ant.network.datasource.search

import com.ant.common.exceptions.bodyOrThrow
import com.ant.common.exceptions.withRetry
import com.ant.models.entities.TvShow
import com.ant.models.request.RequestType
import com.ant.network.mappers.tvseries.TvSeriesMapper
import com.uwetrottmann.tmdb2.Tmdb
import retrofit2.awaitResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchTvShowDataSource @Inject constructor(
    private val tmdb: Tmdb,
    private val tvSeriesMapper: TvSeriesMapper,
) {
    suspend operator fun invoke(params: RequestType.SearchTvShowRequest): List<TvShow> {
        val searchResultResponse = withRetry {
            tmdb.searchService().tv(
                params.query,
                params.page,
                null, // language
                null, // firstAirDateYear
                false, // includeAdult
            )
        }.awaitResponse()

        val genreResponse = tmdb.genreService().tv(null).awaitResponse()

        val searchResults = searchResultResponse.bodyOrThrow()
        val genreResults = genreResponse.bodyOrThrow()

        return tvSeriesMapper.map(Pair(searchResults, genreResults))
    }
}
