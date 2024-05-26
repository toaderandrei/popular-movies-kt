package com.ant.models.source.session

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ant.models.authentication.FirebaseAuthentication
import com.ant.models.session.SessionManager
import kotlinx.coroutines.flow.firstOrNull

class SessionManagerImpl(
    private val dataStore: DataStore<Preferences>,
    private val firebaseAuthentication: FirebaseAuthentication,
) : SessionManager {
    override suspend fun saveSessionId(sessionId: String?): Boolean {
        var isSaved = false
        sessionId?.let {
            dataStore.edit { preferences ->
                preferences[SESSION_ID] = it
                isSaved = true
            }
        }

        return isSaved
    }

    override suspend fun saveUsername(username: String?): Boolean {
        var isSaved = false
        username?.let {
            dataStore.edit { preferences ->
                preferences[USERNAME] = it
                isSaved = true
            }
        }

        return isSaved
    }

    override suspend fun clearSessionAndSignOut(): Boolean {
        val isUserLoggedIn = firebaseAuthentication.getUser() != null
        if (!isUserLoggedIn || getSessionId() == null) {
            return false
        }

        dataStore.edit { preferences ->
            preferences.clear()
        }
        firebaseAuthentication.signOut()
        return true
    }

    override suspend fun isUserLoggedInToTmdbApi(): Boolean {
        return getSessionId() != null
    }

    override fun isUserLoggedInToFirebase(): Boolean {
        return firebaseAuthentication.getUser() != null
    }

    override suspend fun getSessionId(): String? {
        return dataStore.data.firstOrNull()?.get(SESSION_ID)
    }

    override suspend fun getUsername(): String? {
        return dataStore.data.firstOrNull()?.get(USERNAME)
    }

    companion object {
        val SESSION_ID = stringPreferencesKey("SESSION_ID")
        val USERNAME = stringPreferencesKey("USERNAME")

    }
}