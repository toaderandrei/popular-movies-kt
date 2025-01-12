package com.ant.app.ui.compose.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ant.common.logger.TmdbLogger
import com.ant.models.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    logger: TmdbLogger,
    sessionManager: SessionManager,
) : ViewModel() {

    private val _isUserLoggedIn = MutableStateFlow<Boolean?>(null)
    val isUserLoggedIn: StateFlow<Boolean?> get() = _isUserLoggedIn

    init {
        viewModelScope.launch {
            // Fetch initial value synchronously
            logger.d("Checking authentication status.")
            val initialStatus = sessionManager.getUserAuthenticationStatus().firstOrNull()
            _isUserLoggedIn.value = initialStatus ?: false

            // Log and observe changes in real-time
            sessionManager.getUserAuthenticationStatus().onEach { status ->
                logger.d("User login status: $status")
                _isUserLoggedIn.value = status
            }.launchIn(viewModelScope)
        }
    }
}