package com.ant.database.di

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ant.database.database.MoviesDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    private val MIGRATION_40_41 = object : Migration(40, 41) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE moviedata ADD COLUMN synced_to_remote INTEGER DEFAULT 0")
            db.execSQL("ALTER TABLE tvseriesdata ADD COLUMN synced_to_remote INTEGER DEFAULT 0")
        }
    }

    @Provides
    @Singleton
    fun providesDb(application: Application): MoviesDb {
        return createMovieDb(application)
    }

    private fun createMovieDb(application: Application): MoviesDb {
        return Room
            .databaseBuilder(application, MoviesDb::class.java, "tmdb_movies.db")
            .addMigrations(MIGRATION_40_41)
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