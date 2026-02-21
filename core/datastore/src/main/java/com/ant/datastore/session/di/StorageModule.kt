package com.ant.datastore.session.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.ant.common.qualifiers.ApplicationScope
import com.ant.models.session.SessionManager
import com.ant.datastore.session.SessionManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext applicationContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            applicationContext.preferencesDataStoreFile("AppPreferenceStorage")
        }
    }

    @Provides
    @Singleton
    fun provideSessionManager(
        dataStore: DataStore<Preferences>,
        @ApplicationScope scope: CoroutineScope,
    ): SessionManager {
        return SessionManagerImpl(
            scope = scope,
            dataStore = dataStore
        )
    }
}