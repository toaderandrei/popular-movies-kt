package com.ant.models.source.repositories.login

import com.ant.models.authentication.FirebaseAuthentication
import com.ant.models.request.RequestType
import com.ant.models.source.repositories.Repository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IsUserAuthenticatedRepository @Inject constructor(
    private val firebaseAuthentication: FirebaseAuthentication,
) : Repository<Repository.Params<RequestType.FirebaseRequest.GetUser>, Boolean> {
    override suspend fun fetchData(params: Repository.Params<RequestType.FirebaseRequest.GetUser>): Boolean {
        return firebaseAuthentication.getUser() != null
    }
}