package com.ant.app.di

import com.ant.ui.image.CoilInitializer
import com.ant.app.application.TimberInitializer
import com.ant.common.di.Initializer
import com.ant.common.logger.Logger
import com.ant.common.logger.TmdbLogger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class AppModuleBinds {
    @Binds
    @IntoSet
    abstract fun provideTimberInitializer(bind: TimberInitializer): Initializer

    @Singleton
    @Binds
    abstract fun provideLogger(bind: TmdbLogger): Logger

    @Binds
    @IntoSet
    abstract fun provideCoilInitializer(bind: CoilInitializer): Initializer
}