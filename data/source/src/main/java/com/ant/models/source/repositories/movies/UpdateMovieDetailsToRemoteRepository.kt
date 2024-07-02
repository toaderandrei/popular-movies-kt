package com.ant.models.source.repositories.movies

import com.ant.models.request.RequestType
import com.ant.models.source.datasource.movies.UpdateMovieAsFavoriteDataSource
import com.ant.models.source.repositories.Repository
import com.uwetrottmann.tmdb2.Tmdb
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class UpdateMovieDetailsToRemoteRepository @Inject constructor(
    private val tmdb: Tmdb,
) : Repository<Repository.Params<RequestType.SaveMovieRequest>, Boolean> {
    override suspend fun performRequest(params: Repository.Params<RequestType.SaveMovieRequest>): Boolean {
        return UpdateMovieAsFavoriteDataSource(params = params, tmdb = tmdb).invoke()
    }
}