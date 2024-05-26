package com.ant.models.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "loginsession",
    indices = [
        Index(value = ["id"], unique = true)
    ],
)
data class LoginSession(
    @PrimaryKey
    @ColumnInfo(name = "id")
    override val id: Long = 0,

    @ColumnInfo(name = "session_id")
    val sessionId: String? = null,

    @ColumnInfo(name = "is_firebase_registered")
    val isFirebaseRegistered: Boolean? = false,

    @ColumnInfo(name = "success")
    val success: Boolean? = false,
) : TmdbEntity {
    @Ignore
    constructor() : this(id = 0)
}