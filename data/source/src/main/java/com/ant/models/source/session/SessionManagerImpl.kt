package com.ant.models.source.session

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ant.models.session.SessionManager
import kotlinx.coroutines.flow.firstOrNull

class SessionManagerImpl(
    private val dataStore: DataStore<Preferences>,
) : SessionManager {
    override suspend fun saveSessionId(sessionId: String?): Boolean {
        return saveInternal(SESSION_ID, sessionId)
    }

    override suspend fun saveUsername(username: String?): Boolean {
        return saveInternal(USERNAME, username)
    }

    private suspend fun saveInternal(key: Preferences.Key<String>, value: String?): Boolean {
        var isSaved = false
        value?.let {
            dataStore.edit { preferences ->
                preferences[key] = it
                isSaved = true
            }
        }
        return isSaved
    }

    override suspend fun clearSessionAndSignOut(): Boolean {
        if (getSessionId() == null) {
            return false
        }

        dataStore.edit { preferences ->
            preferences.clear()
        }
        return true
    }

    override suspend fun isUserLoggedInToTmdbApi(): Boolean {
        return getSessionId() != null
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
        val AVATAR = stringPreferencesKey("AVATAR")
    }
}