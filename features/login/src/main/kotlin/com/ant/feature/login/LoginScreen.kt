package com.ant.feature.login

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ant.feature.login.state.getErrorOrNull
import com.ant.feature.login.state.isError
import com.ant.feature.login.state.isLoading
import com.ant.feature.login.state.isSuccess
import com.ant.resources.R as R2

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {

    val loginState by viewModel.stateAsFlow.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(remember { SnackbarHostState() }) }
    ) {
        var credentials by remember { mutableStateOf(Credentials()) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R2.string.login_tmdb),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(24.dp)
            )

            // username
            // Username Input Field
            OutlinedTextField(
                value = credentials.username,
                onValueChange = { credentials = credentials.copy(username = it) },
                label = { Text(stringResource(R2.string.login)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // password
            // Password Input Field
            OutlinedTextField(
                value = credentials.password,
                onValueChange = { credentials = credentials.copy(password = it) },
                label = { Text(stringResource(R2.string.password)) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    viewModel.login(credentials.username, credentials.password)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !loginState.isLoading
            ) {
                if (loginState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = stringResource(R2.string.login)
                    )
                }
            }

            if (loginState.isError) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = loginState.getErrorOrNull()?.message
                        ?: stringResource(R2.string.error_login),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sign Up Prompt
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = stringResource(R2.string.no_account))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(R2.string.signup))
            }
        }
    }
    // Handle successful login state
    if (loginState.isSuccess) {
        LaunchedEffect(Unit) {
            onLoginSuccess()
        }
    }

}

