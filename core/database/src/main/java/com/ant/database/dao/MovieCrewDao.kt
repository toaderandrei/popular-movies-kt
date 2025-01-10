package com.ant.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ant.models.entities.MovieCrew
import com.ant.models.entities.MovieCrews

@Dao
abstract class MovieCrewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg movie: MovieCrew)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(movie: MovieCrew)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(movieData: List<MovieCrew>)

    @Query("SELECT * FROM MovieCrew WHERE  movie_id = :movieId")
    abstract suspend fun loadMovieCrews(movieId: Long): MovieCrews
}