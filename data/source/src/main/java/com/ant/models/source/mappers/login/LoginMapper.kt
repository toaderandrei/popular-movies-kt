package com.ant.models.source.mappers.login

import com.ant.models.model.UserData
import com.ant.models.source.mappers.Mapper
import com.uwetrottmann.tmdb2.entities.Account
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginMapper @Inject constructor() : Mapper<Account, UserData> {
    override suspend fun map(from: Account): UserData {
        return UserData(
            username = from.name,
        )
    }
}