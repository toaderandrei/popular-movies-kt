package com.ant.app.di

import com.ant.models.authentication.FirebaseAuthentication
import com.ant.models.source.authentication.FirebaseAuthenticationImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthenticationModule {

    @Singleton
    @Provides
    fun provideFirebaseAuthentication(firebaseAuth: FirebaseAuth): FirebaseAuthentication {
        return FirebaseAuthenticationImpl(firebaseAuth)
    }

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
}