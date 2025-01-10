package com.ant.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworksModule {
    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder().build()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.apply { loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY }
        return okHttpClient.newBuilder()
            .addInterceptor(loggingInterceptor)
            .retryOnConnectionFailure(false)
            .connectTimeout(35L, TimeUnit.SECONDS)
            .readTimeout(35L, TimeUnit.SECONDS)
            .build()
    }
}