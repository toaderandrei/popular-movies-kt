package com.ant.models.source.session

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ant.models.session.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SessionManagerImpl(
    private val dataStore: DataStore<Preferences>,
) : SessionManager {
    private val _isLoggedInFlow = MutableStateFlow(false)
    override val isLoggedInFlow: StateFlow<Boolean> get() = _isLoggedInFlow

    init {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.data
                .map { preferences -> preferences[SESSION_ID] != null }
                .collect { isLoggedIn -> _isLoggedInFlow.value = isLoggedIn }
        }
    }

    override suspend fun saveSessionId(sessionId: String?): Boolean {
        return saveInternal(SESSION_ID, sessionId).also {
            _isLoggedInFlow.value = sessionId != null
        }
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
        _isLoggedInFlow.value = false
        return true
    }

    override suspend fun isUserLoggedInToTmdbApi(): Boolean {
        return _isLoggedInFlow.value
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