package com.ant.database.di

import android.app.Application
import androidx.room.Room
import com.ant.database.database.MoviesDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun providesDb(application: Application): MoviesDb {
        return createMovieDb(application)
    }

    private fun createMovieDb(application: Application): MoviesDb {
        return Room
            .databaseBuilder(application, MoviesDb::class.java, "tmdb_movies.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseDaoModule {
    @Provides
    fun providesPopularMovies(db: MoviesDb) = db.moviesDao()

    @Provides
    fun providesPopularTvSeries(db: MoviesDb) = db.tvSeriesDao()
}