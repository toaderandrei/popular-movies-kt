package com.ant.feature.login.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ant.resources.R as R2

@Composable
fun AccountRoute(
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AccountViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.logoutSuccess) {
        if (uiState.logoutSuccess) {
            onNavigateBack()
        }
    }

    AccountScreen(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onNavigateToLogin = onNavigateToLogin,
        onLogout = viewModel::logout,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    uiState: AccountUiState,
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R2.string.account)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R2.string.navigate_back),
                        )
                    }
                },
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center,
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator()
                }

                uiState.isLoggedIn -> {
                    LoggedInContent(
                        username = uiState.username,
                        isLoggingOut = uiState.isLoggingOut,
                        onLogout = onLogout,
                    )
                }

                else -> {
                    NotLoggedInContent(
                        onNavigateToLogin = onNavigateToLogin,
                    )
                }
            }
        }
    }
}

@Composable
private fun LoggedInContent(
    username: String?,
    isLoggingOut: Boolean,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(R2.drawable.user_login_avatar),
            contentDescription = null,
            modifier = Modifier.size(200.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R2.string.username_account, username ?: "User"),
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R2.string.account_is_logged_in_title),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoggingOut,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
            ),
        ) {
            if (isLoggingOut) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onError,
                )
            } else {
                Text(stringResource(R2.string.logout))
            }
        }
    }
}

@Composable
private fun NotLoggedInContent(
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(R2.drawable.image_login),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R2.string.login_title),
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onNavigateToLogin,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            Text(stringResource(R2.string.login))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AccountScreenLoggedInPreview() {
    MaterialTheme {
        AccountScreen(
            uiState = AccountUiState(
                isLoading = false,
                isLoggedIn = true,
                username = "john_doe",
                sessionId = "abc123",
            ),
            onNavigateBack = {},
            onNavigateToLogin = {},
            onLogout = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AccountScreenNotLoggedInPreview() {
    MaterialTheme {
        AccountScreen(
            uiState = AccountUiState(
                isLoading = false,
                isLoggedIn = false,
            ),
            onNavigateBack = {},
            onNavigateToLogin = {},
            onLogout = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AccountScreenLoadingPreview() {
    MaterialTheme {
        AccountScreen(
            uiState = AccountUiState(isLoading = true),
            onNavigateBack = {},
            onNavigateToLogin = {},
            onLogout = {},
        )
    }
}
