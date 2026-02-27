package com.ant.network.mappers.login

import com.ant.models.model.UserData
import com.ant.network.mappers.Mapper
import com.uwetrottmann.tmdb2.entities.Session
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginSessionMapper @Inject constructor() : Mapper<Session, UserData> {
    override suspend fun map(from: Session): UserData {
        return UserData(
            sessionId = from.session_id,
        )
    }
}