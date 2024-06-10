package com.ant.domain.usecases.login

import com.ant.common.logger.TmdbLogger
import com.ant.domain.qualifiers.IoDispatcher
import com.ant.domain.usecases.UseCase
import com.ant.models.authentication.FirebaseAuthentication
import com.ant.models.entities.LoginSession
import com.ant.models.request.RequestType
import com.ant.models.source.repositories.Repository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineDispatcher
import java.lang.IllegalArgumentException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.log

@Singleton
class LoginUserToFirebaseUseCase @Inject constructor(
    private val firebaseAuthentication: FirebaseAuthentication,
    private val logger: TmdbLogger,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Repository.Params<RequestType.FirebaseRequest.SignIn>, LoginSession>(dispatcher) {
    override suspend fun execute(parameters: Repository.Params<RequestType.FirebaseRequest.SignIn>): LoginSession {
        val email = parameters.request.email
        val password = parameters.request.password
        if (email == null || password == null) {
            logger.d("Email or password is null")
            throw IllegalArgumentException("Email or password is null")
        }
        var firebaseUser = firebaseAuthentication.signInWithEmailPassword(
            email = email,
            password = password,
        )

        logger.d("firebaseUser: $firebaseUser")

        if (firebaseUser == null) {
            firebaseUser =
                firebaseAuthentication.signUpWithEmailPassword(email = email, password = password)
        }
        return LoginSession(
            isFirebaseRegistered = firebaseUser != null,
        )
    }
}