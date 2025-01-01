package com.ant.models.source.dao

import androidx.room.Transaction
import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Dao
import com.ant.models.entities.MovieCast

@Dao
abstract class MovieCastsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg movie: MovieCast)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(movie: MovieCast)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(vararg movieCasts: MovieCast)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(movieCasts: List<MovieCast>)

    @Query("SELECT * FROM MovieCast WHERE  movie_id = :movieId")
    abstract suspend fun loadMovieCastsForMovieId(movieId: Long): List<MovieCast>

    @Query("DELETE FROM MovieCast WHERE  movie_id = :movieId")
    abstract suspend fun deleteMovieCastsById(movieId: Long)
}