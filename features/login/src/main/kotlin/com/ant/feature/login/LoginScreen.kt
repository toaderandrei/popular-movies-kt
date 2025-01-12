package com.ant.feature.login

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ant.feature.login.state.getErrorOrNull
import com.ant.feature.login.state.isCanceled
import com.ant.feature.login.state.isError
import com.ant.feature.login.state.isLoading
import com.ant.feature.login.state.isSuccess
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ant.resources.R as R2

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onLoginCanceled: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val loginState by viewModel.longStateFlow.collectAsStateWithLifecycle()
    val systemUiController = rememberSystemUiController()

    val statusBarColor = colorResource(id = R2.color.md_light_blue_100) // Light Blue

    // Set status bar color to transparent
    LaunchedEffect(Unit) {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = false // Adjust based on your gradient's brightness
        )
    }

    Scaffold(modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(remember { SnackbarHostState() }) }) {
        var credentials by remember { mutableStateOf(Credentials()) }
        var passwordVisible by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(
                    brush = Brush.verticalGradient(
                        startY = 0.0f,
                        endY = Float.POSITIVE_INFINITY, // Extend gradient across full height
                        colors = listOf(
                            colorResource(id = R2.color.md_light_blue_100), // Light Blue
                            colorResource(id = R2.color.md_blue_50)
                        )
                    )
                ),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R2.dimen.padding_16)),

                ) {
                Text(
                    text = stringResource(R2.string.login_tmdb),
                    style = MaterialTheme.typography.headlineLarge,
                )

                // username
                OutlinedTextField(
                    value = credentials.username,
                    onValueChange = { credentials = credentials.copy(username = it) },
                    label = { Text(stringResource(R2.string.username_account_title)) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Black,
                        focusedTextColor = Color.Black,
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(dimensionResource(R2.dimen.height_12)))

                // password
                OutlinedTextField(
                    value = credentials.password,
                    onValueChange = { credentials = credentials.copy(password = it) },
                    label = { Text(stringResource(R2.string.password)) },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Black,
                        focusedTextColor = Color.Black,
                    ),
                    trailingIcon = {
                        if (passwordVisible) {
                            IconButton(onClick = {
                                passwordVisible = false
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Visibility,
                                    contentDescription = "show_password"
                                )
                            }
                        } else {
                            IconButton(onClick = {
                                passwordVisible = true
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.VisibilityOff,
                                    contentDescription = "hide_password"
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(dimensionResource(R2.dimen.margin_24)))
                Button(
                    onClick = {
                        viewModel.login(credentials.username, credentials.password)
                    }, modifier = Modifier.fillMaxWidth(), enabled = !loginState.isLoading
                ) {
                    if (loginState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(dimensionResource(R2.dimen.width_100))
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

                Spacer(modifier = Modifier.height(dimensionResource(R2.dimen.margin_24)))

                // Sign Up Prompt
                Row(horizontalArrangement = Arrangement.Start) {
                    Text(text = stringResource(R2.string.no_account))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(R2.string.signup))
                }

                Spacer(modifier = Modifier.height(dimensionResource(R2.dimen.margin_8)))

                Text(
                    text = stringResource(R2.string.continue_local),
                    modifier = Modifier.clickable {
                        viewModel.cancelLogin()
                    }
                )
            }
        }
    }
    //
    // Handle successful login state
    if (loginState.isCanceled) {
        LaunchedEffect(Unit) {
            onLoginCanceled()
        }
    }
    if (loginState.isSuccess) {
        LaunchedEffect(Unit) {
            onLoginSuccess()
        }
    }
}

