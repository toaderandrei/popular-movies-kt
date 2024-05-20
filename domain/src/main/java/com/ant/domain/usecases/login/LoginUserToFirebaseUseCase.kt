package com.ant.domain.usecases.login

import com.ant.domain.qualifiers.IoDispatcher
import com.ant.domain.usecases.UseCase
import com.ant.models.authentication.FirebaseAuthentication
import com.ant.models.entities.LoginSession
import com.ant.models.request.RequestType
import com.ant.models.source.repositories.Repository
import com.ant.models.source.repositories.login.AuthenticateTmDbRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginUserToFirebaseUseCase @Inject constructor(
    private val firebaseAuthentication: FirebaseAuthentication,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Repository.Params<RequestType.FirebaseRequest.SignIn>, FirebaseUser?>(dispatcher) {
    override suspend fun execute(parameters: Repository.Params<RequestType.FirebaseRequest.SignIn>): FirebaseUser? {
        return firebaseAuthentication.signInWithEmailPassword(
            parameters.request.username,
            parameters.request.password
        )
    }

}