package com.ant.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ant.models.entities.MovieData

@Dao
abstract class MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg movie: MovieData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(movie: MovieData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(movieData: List<MovieData>)

    @Query("SELECT * FROM MovieData WHERE id = :id")
    abstract fun findMovieById(id: Int?): MovieData?

    @Query("DELETE FROM movieData where id =:id")
    abstract fun deleteMovieById(id: Long)

    @Query("SELECT * from MovieData where favored=:favored")
    abstract fun loadFavoredMovies(favored: Boolean): List<MovieData>
}