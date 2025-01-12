package com.ant.datastore.session

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ant.models.session.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SessionManagerImpl(
    private val dataStore: DataStore<Preferences>,
) : SessionManager {

    private val _userAuthenticationStatus = dataStore.data
        .map { preferences -> preferences[SESSION_ID] != null }
        .stateIn(
            scope = CoroutineScope(Dispatchers.IO),
            started = SharingStarted.Eagerly,
            initialValue = null // Use null to represent "loading"
        )


    override suspend fun saveSessionId(sessionId: String?): Boolean {
        return saveInternal(SESSION_ID, sessionId)
    }

    override suspend fun saveUsername(username: String?): Boolean {
        return saveInternal(USERNAME, username)
    }

    private suspend fun saveInternal(key: Preferences.Key<String>, value: String?): Boolean {
        return value?.let {
            dataStore.edit { preferences ->
                preferences[key] = it
            }
            true
        } ?: false
    }

    override suspend fun clearSessionAndSignOut(): Boolean {
        if (getSessionId() == null) return false
        dataStore.edit { it.clear() }
        return true
    }

    override fun getUserAuthenticationStatus(): Flow<Boolean?> {
        return _userAuthenticationStatus
    }

    override suspend fun getSessionId(): String? {
        return dataStore.data.firstOrNull()?.get(SESSION_ID)
    }

    override suspend fun getUsername(): String? {
        return dataStore.data.firstOrNull()?.get(USERNAME)
    }

    companion object {
        val SESSION_ID = stringPreferencesKey(SessionManager.SESSION_ID)
        val USERNAME = stringPreferencesKey(SessionManager.USERNAME)
    }
}