package com.ant.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ant.models.entities.TvShow

@Dao
abstract class TvSeriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg tvSeriesData: TvShow)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(tvSeriesData: TvShow)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(tvSeriesData: List<TvShow>)

    @Query("SELECT * FROM TvSeriesData WHERE id = :id")
    abstract fun findTvSeriesById(id: Int?): TvShow?

    @Query("DELETE FROM TvSeriesData where id =:id")
    abstract fun deleteTvSeriesById(id: Long)

    @Query("SELECT * from TvSeriesData where favored=:favored")
    abstract fun loadFavoredTvSeriesData(favored: Boolean): List<TvShow>

    @Query("UPDATE tvseriesdata SET synced_to_remote = :synced WHERE id = :id")
    abstract suspend fun updateSyncStatus(id: Long, synced: Boolean)
}