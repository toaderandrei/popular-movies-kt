package com.ant.app.di

import android.content.Context
import android.text.format.DateFormat
import com.ant.app.BuildConfig
import com.ant.app.application.PopularMoviesApp
import com.ant.tmdb.old.TmdbModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Named
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module(
    includes = [
        CoroutinesModule::class, NetworksModule::class, AppModuleBinds::class, TmdbModule::class
    ]
)
object AppModule {

    @Provides
    fun provideContext(application: PopularMoviesApp): Context = application.applicationContext

    // todo verify the date formatter.
    @Singleton
    @Provides
    fun provideDateTimeFormatter(application: PopularMoviesApp): DateTimeFormatter {
        val dateF = DateFormat.getMediumDateFormat(application) as SimpleDateFormat
        val timeF = DateFormat.getTimeFormat(application) as SimpleDateFormat
        return DateTimeFormatter.ofPattern("${dateF.toPattern()} ${timeF.toPattern()}")
            .withLocale(Locale.getDefault())
            .withZone(ZoneId.systemDefault())
    }

    @Singleton
    @Provides
    fun provideShortDateFormatter(application: PopularMoviesApp): DateTimeFormatter {
        val dateF = DateFormat.getDateFormat(application) as SimpleDateFormat
        return DateTimeFormatter.ofPattern(dateF.toPattern())
            .withLocale(Locale.getDefault())
            .withZone(ZoneId.systemDefault())
    }

    @Provides
    @Named("api_key")
    fun provideTmdbApiKey(): String = BuildConfig.TMDB_API_KEY
}