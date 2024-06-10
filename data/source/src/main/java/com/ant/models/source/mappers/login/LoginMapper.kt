package com.ant.models.source.mappers.login

import com.ant.models.entities.LoginData
import com.ant.models.source.mappers.Mapper
import com.uwetrottmann.tmdb2.entities.Account
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginMapper @Inject constructor() : Mapper<Account, LoginData> {
    override suspend fun map(from: Account): LoginData {
        return LoginData(
            from.id!!.toLong(),
            username = from.name,
        )
    }
}