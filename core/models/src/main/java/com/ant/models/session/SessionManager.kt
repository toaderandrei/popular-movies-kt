package com.ant.models.session

import kotlinx.coroutines.flow.Flow

interface SessionManager {

    /**
     * Save session id to data store and to firebase.
     * @param sessionId: String?
     * @return Boolean true if saved, false otherwise.
     */
    suspend fun saveSessionId(sessionId: String?): Boolean


    suspend fun saveUsername(username: String?): Boolean

    /**
     * Clear session and sign out from data store and firebase.
     * @return Boolean true if signed out, false otherwise.
     */
    suspend fun clearSessionAndSignOut(): Boolean

    /**
     * Check if user is logged in with firebase.
     * @return Boolean true if logged in, false otherwise.
     */
    fun getUserAuthenticationStatus(): Flow<Boolean?>

    /**
     * Retrieves the current session id.
     */
    suspend fun getSessionId(): String?

    /**
     * Retrieves the current user.
     */
    suspend fun getUsername(): String?

    suspend fun setGuestMode(enabled: Boolean): Boolean

    fun isGuestMode(): Flow<Boolean>

    /**
     * Returns true if the user can skip authentication (logged in OR guest mode).
     * Returns null while loading.
     */
    fun canSkipAuthentication(): Flow<Boolean?>

    companion object {
        const val SESSION_ID = "session_id"
        const val USERNAME = "username"
        const val AVATAR = "avatar"
        const val GUEST_MODE = "guest_mode"
    }
}