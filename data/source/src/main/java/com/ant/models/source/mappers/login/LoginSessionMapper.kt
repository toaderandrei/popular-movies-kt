package com.ant.models.source.mappers.login

import com.ant.models.model.UserData
import com.ant.models.source.mappers.Mapper
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