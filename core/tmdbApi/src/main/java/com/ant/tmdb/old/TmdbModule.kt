package com.ant.tmdb.old

import com.uwetrottmann.tmdb2.Tmdb
import com.uwetrottmann.tmdb2.TmdbHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TmdbModule {

    @Provides
    fun provideTmdbImageUrlProvider(): TmdbImageUrlProvider {
        return TmdbImageUrlProvider()
    }

    @Singleton
    @Provides
    fun provideTmdbApi(
        client: OkHttpClient,
        @Named("api_key") apiKey: String
    ): Tmdb {
        return object : Tmdb(apiKey) {
            override fun okHttpClient(): OkHttpClient {
                return client.newBuilder().apply {
                    setOkHttpClientDefaults(this)
                    connectTimeout(5, TimeUnit.SECONDS)
                    readTimeout(5, TimeUnit.SECONDS)
                    writeTimeout(5, TimeUnit.SECONDS)
                }.build()
            }

            override fun retrofitBuilder(): Retrofit.Builder {
                return Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(
                        GsonConverterFactory.create(
                            TmdbHelper.getGsonBuilder().create()
                        )
                    )
                    .client(okHttpClient())
            }
        }
    }

}