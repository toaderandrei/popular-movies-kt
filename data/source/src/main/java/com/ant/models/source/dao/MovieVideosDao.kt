package com.ant.models.source.dao

import androidx.room.Transaction
import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Dao
import com.ant.models.entities.MovieVideo

@Dao
abstract class MovieVideosDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg movie: MovieVideo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(movie: MovieVideo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(vararg movieVideos: MovieVideo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(movieVideos: List<MovieVideo>)

    @Query("SELECT * FROM MovieVideo WHERE  movie_id = :movieId")
    abstract suspend fun loadVideosForMovieId(movieId: Long): List<MovieVideo>

    @Query("DELETE FROM MovieVideo WHERE  movie_id = :movieId")
    abstract suspend fun deleteMovieVideosById(movieId: Long)

    @Transaction
    open suspend fun withTransaction(tx: suspend () -> Unit) = tx()
}