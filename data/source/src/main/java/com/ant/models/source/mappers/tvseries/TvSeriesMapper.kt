package com.ant.models.source.mappers.tvseries

import com.ant.models.entities.TvShow
import com.ant.models.source.mappers.Mapper
import com.uwetrottmann.tmdb2.entities.TvShowResultsPage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TvSeriesMapper @Inject constructor(
    private val tvSeriesDataMapper: TvSeriesDataMapper
) : Mapper<TvShowResultsPage, List<TvShow>> {
    override suspend fun map(from: TvShowResultsPage): List<TvShow> {
        return from.results?.map {
            tvSeriesDataMapper.map(it)
        } ?: emptyList()
    }
}