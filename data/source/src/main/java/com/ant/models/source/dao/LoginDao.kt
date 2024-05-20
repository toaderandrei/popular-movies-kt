package com.ant.models.source.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ant.models.entities.LoginData


@Dao
abstract class LoginDao {
    @Query("SELECT * FROM logindata WHERE username = :username")
    abstract fun getUserByName(username: String): LoginData

    @Query("SELECT * FROM logindata WHERE id = :id")
    abstract fun getUserByName(id: Long): LoginData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg loginData: LoginData)
}

