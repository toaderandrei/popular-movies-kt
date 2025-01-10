package com.ant.ui.image

import coil.annotation.ExperimentalCoilApi
import coil.intercept.Interceptor
import coil.request.ImageResult
import com.ant.models.entities.ImageEntity
import com.ant.tmdb.old.TmdbImageUrlProvider
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import javax.inject.Inject
import javax.inject.Provider

@ExperimentalCoilApi
class TmdbImageCoilInterceptor @Inject constructor(
    private val tmdbImageUrlProvider: Provider<TmdbImageUrlProvider>,
) : Interceptor {
    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val request = when (val data = chain.request.data) {
            is ImageEntity -> {
                chain.request.newBuilder()
                    .data(map(data))
                    .build()
            }
            else -> chain.request
        }
        return chain.proceed(request)
    }

    private fun map(imageEntity: ImageEntity): HttpUrl {
        val urlProvider = tmdbImageUrlProvider.get()
        return urlProvider.getUrl(
            imageEntity.path,
            imageEntity.imageOptions
        ).toHttpUrl()
    }
}
