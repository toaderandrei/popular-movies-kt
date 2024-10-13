package com.ant.app.di

import android.app.Activity
import android.content.Context
import com.ant.common.listeners.ToolbarWithNavigation
import com.ant.common.logger.TmdbLogger
import com.ant.core.ui.ToolbarNavigationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(FragmentComponent::class)
object FragmentModule {

    @Provides
    fun provideToolbarWithNavigation(
        @ActivityContext context: Context
    ): ToolbarWithNavigation {
        return context as ToolbarWithNavigation // Cast context to ToolbarWithNavigation
    }

    @Provides
    fun provideToolbarWithNavigationManager(
        logger: TmdbLogger,
        toolbarWithNavigation: ToolbarWithNavigation
    ): ToolbarNavigationManager {
        return ToolbarNavigationManager(logger, toolbarWithNavigation)
    }
}