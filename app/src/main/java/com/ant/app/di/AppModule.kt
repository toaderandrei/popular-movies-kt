package com.ant.app.di

import android.app.Activity
import android.content.Context
import android.text.format.DateFormat
import com.ant.analytics.di.AnalyticsModule
import com.ant.app.BuildConfig
import com.ant.app.application.PopularMoviesApp
import com.ant.common.listeners.ToolbarWithNavigation
import com.ant.common.logger.TmdbLogger
import com.ant.core.ui.ToolbarNavigationManager
import com.ant.tmdb.old.TmdbModule
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.components.SingletonComponent
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Named
import javax.inject.Singleton
import kotlin.math.log


@InstallIn(SingletonComponent::class)
@Module(
    includes = [
        CoroutinesModule::class,
        NetworksModule::class,
        AppModuleBinds::class,
        StorageModule::class,
        AuthenticationModule::class,
        AnalyticsModule::class,
        TmdbModule::class,
        FragmentModule::class,
    ]
)
object AppModule {

    @Provides
    fun provideContext(application: PopularMoviesApp): Context = application.applicationContext

    @Provides
    fun provideFirebaseAnalytics(): FirebaseAnalytics {
        return Firebase.analytics
    }

    @Provides
    fun provideFirebaseCrashlytics(): FirebaseCrashlytics {
        return FirebaseCrashlytics.getInstance()
    }

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

    /**All of our application dependencies shall be provided here*/
}