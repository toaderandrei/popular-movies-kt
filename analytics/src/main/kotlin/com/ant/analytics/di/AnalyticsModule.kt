package com.ant.analytics.di

import com.ant.analytics.AnalyticsHelper
import com.ant.analytics.AnalyticsHelperImpl
import com.ant.analytics.CrashlyticsHelper
import com.ant.analytics.CrashlyticsHelperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {

    @Binds
    abstract fun bindsAnalyticsHelper(analyticsHelperImpl: AnalyticsHelperImpl): AnalyticsHelper

    @Binds
    abstract fun bindsCrashlyticsHelper(crashlyticsHelper: CrashlyticsHelperImpl): CrashlyticsHelper
}
