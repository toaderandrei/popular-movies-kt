package com.ant.app.ui.extensions

import com.ant.models.entities.LoginSession
import com.ant.models.model.LoginState
import com.ant.models.model.Result
import com.ant.models.model.getErrorOrNull
import com.ant.models.model.isLoading
import com.ant.models.model.isSuccess

fun LoginState.parseResponse(
    response: Result<LoginSession>,
    isTmdb: Boolean,
): LoginState {
    return when (isTmdb) {
        true -> {
            parseTmDbLogin(response)
        }

        false -> {
            parseFirebaseResponse(response)
        }
    }
}

private fun LoginState.parseTmDbLogin(it: Result<LoginSession>): LoginState {
    return when {
        it.isLoading -> {
            copy(
                tmdbLogin = tmdbLogin.copy(loading = true, data = null, error = null)
            )
        }

        it.isSuccess -> {
            copy(
                tmdbLogin = tmdbLogin.copy(loading = false, data = it.get(), error = null)
            )
        }

        else -> {
            copy(
                tmdbLogin = tmdbLogin.copy(
                    loading = false,
                    data = null,
                    error = it.getErrorOrNull(),
                )
            )
        }
    }
}

private fun LoginState.parseFirebaseResponse(it: Result<LoginSession>): LoginState {
    return when {
        it.isLoading -> {
            copy(
                firebaseLogin = firebaseLogin.copy(loading = true, data = null, error = null)
            )
        }

        it.isSuccess -> {
            copy(
                firebaseLogin = firebaseLogin.copy(loading = false, data = it.get(), error = null)
            )
        }

        else -> {
            copy(
                firebaseLogin = firebaseLogin.copy(
                    loading = false,
                    data = null,
                    error = it.getErrorOrNull(),
                )
            )
        }
    }
}