package com.ant.app.ui.compose.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ant.common.logger.TmdbLogger
import com.ant.models.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    logger: TmdbLogger,
    sessionManager: SessionManager,
) : ViewModel() {

    private val _isUserLoggedIn = MutableStateFlow<Boolean?>(null)
    val isUserLoggedIn: StateFlow<Boolean?> = _isUserLoggedIn.asStateFlow()

    init {
        viewModelScope.launch {
            logger.d("Checking authentication status.")
            sessionManager.canSkipAuthentication()
                .filterNotNull()
                .collect { status ->
                    logger.d("User login status: $status")
                    _isUserLoggedIn.value = status
                }
        }
    }
}
