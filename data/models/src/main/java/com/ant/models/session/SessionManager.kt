package com.ant.models.session

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
    suspend fun isUserLoggedInToTmdbApi(): Boolean

    /**
     * Retrieves the current session id.
     */
    suspend fun getSessionId(): String?

    /**
     * Retrieves the current user.
     */
    suspend fun getUsername(): String?
}