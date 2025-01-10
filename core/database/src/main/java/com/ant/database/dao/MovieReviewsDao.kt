package com.ant.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ant.models.entities.MovieReview
import com.ant.models.entities.MovieReviews

@Dao
abstract class MovieReviewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg movie: MovieReview)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(movie: MovieReview)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(movieData: List<MovieReview>)

    @Query("SELECT * FROM MovieReview WHERE  movie_id = :movieId")
    abstract suspend fun loadMovieReviews(movieId: Long): MovieReviews
}