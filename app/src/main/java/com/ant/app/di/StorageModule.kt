package com.ant.app.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.ant.models.authentication.FirebaseAuthentication
import com.ant.models.session.SessionManager
import com.ant.models.source.delegates.SessionManagerDelegate
import com.ant.models.source.session.SessionManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
        firebaseAuthentication: FirebaseAuthentication,
    ): SessionManager {
        return SessionManagerImpl(dataStore = dataStore, firebaseAuthentication = firebaseAuthentication)
    }

    @Singleton
    @Provides
    fun provideSessionManagerDelegate(sessionManager: SessionManager): SessionManagerDelegate {
        return SessionManagerDelegate(sessionManager)
    }
}