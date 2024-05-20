package com.ant.models.source.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ant.models.entities.LoginData
import com.ant.models.entities.LoginSession

@Dao
abstract class LoginSessionDao {
    @Query("SELECT * FROM loginsession WHERE id = :id")
    abstract fun getUserSessionById(id: Long): LoginSession

    @Query("SELECT * FROM loginsession WHERE session_id = :sessionId")
    abstract fun getUserSessionByName(sessionId: String): LoginSession

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg loginData: LoginSession)
}

