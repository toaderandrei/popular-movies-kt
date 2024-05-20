package com.ant.models.source.mappers.login

import com.ant.models.entities.LoginSession
import com.ant.models.source.mappers.Mapper
import com.uwetrottmann.tmdb2.entities.Session
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginSessionMapper @Inject constructor() : Mapper<Session, LoginSession> {
    override suspend fun map(from: Session): LoginSession {
        return LoginSession(
            sessionId = from.session_id,
            success = from.success
        )
    }
}