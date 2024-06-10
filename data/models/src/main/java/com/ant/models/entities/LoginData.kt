package com.ant.models.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "logindata",
    indices = [
        Index(value = ["id"], unique = true)
    ],
)
data class LoginData(
    @PrimaryKey
    @ColumnInfo(name = "id")
    override val id: Long = 0,

    @ColumnInfo(name = "username")
    val username: String? = null,

    @ColumnInfo(name = "session_id")
    val session: String? = null
) : TmdbEntity {

    @Ignore
    constructor() : this(id = 0)
}