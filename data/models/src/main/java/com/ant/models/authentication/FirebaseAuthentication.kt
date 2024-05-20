package com.ant.models.authentication

import com.google.firebase.auth.FirebaseUser

interface FirebaseAuthentication {

    /**
     * Sign up with email and password
     */

    suspend fun signUpWithEmailPassword(email: String, password: String): FirebaseUser?

    /**
     * Sign in with email and password
     */
    suspend fun signInWithEmailPassword(email: String, password: String): FirebaseUser?

    /**
     * Sign out
     */
    fun signOut(): FirebaseUser?

    /**
     * Get the current user.
     */
    fun getUser(): FirebaseUser?

    /**
     * Send a password reset email.
     */
    suspend fun sendPasswordReset(email: String)
}