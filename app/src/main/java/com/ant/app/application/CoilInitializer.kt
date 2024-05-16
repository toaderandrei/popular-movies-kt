package com.ant.app.application

import android.content.Context
import coil.Coil
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import com.ant.app.image.TmdbImageCoilInterceptor
import com.ant.common.di.Initializer
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import javax.inject.Inject

@OptIn(ExperimentalCoilApi::class)
class CoilInitializer @Inject constructor(
    @ApplicationContext private val context: Context,
    private val okHttpClient: OkHttpClient,
    private val tmdbImageCoilInterceptor: TmdbImageCoilInterceptor,
) : Initializer {
    override fun init() {
        Coil.setImageLoader(
            ImageLoader.Builder(context)
                .components {
                    add(tmdbImageCoilInterceptor)
                }
                .okHttpClient(okHttpClient)
                .build()
        )
    }
}