package com.ant.models.model

import com.ant.models.entities.LoginSession

data class LoginState(
    val tmdbLogin: MoviesState<LoginSession> = MoviesState(),
    val firebaseLogin: MoviesState<LoginSession> = MoviesState(),
)


