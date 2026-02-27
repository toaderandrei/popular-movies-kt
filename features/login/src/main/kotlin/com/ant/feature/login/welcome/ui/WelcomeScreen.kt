package com.ant.feature.login.welcome.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ant.feature.login.welcome.WelcomeUiState
import com.ant.resources.R as R2

@Composable
fun WelcomeScreen(
    uiState: WelcomeUiState,
    onLoginClick: () -> Unit,
    onContinueAsGuestClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A2E)),
    ) {
        // Background image
        if (uiState.backdropUrl != null) {
            AsyncImage(
                model = uiState.backdropUrl,
                contentDescription = stringResource(R2.string.movie_image_desc),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }

        // Gradient scrim overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.7f),
                            Color.Black.copy(alpha = 0.9f),
                        ),
                    ),
                ),
        )

        when {
            uiState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White,
                )
            }

            else -> {
                // Content at the bottom
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 24.dp, vertical = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        text = stringResource(R2.string.welcome_title),
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White,
                    )
                    Text(
                        text = stringResource(R2.string.welcome_subtitle),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = onLoginClick,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(stringResource(R2.string.login_with_tmdb))
                    }

                    OutlinedButton(
                        onClick = onContinueAsGuestClick,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White,
                        ),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.7f)),
                    ) {
                        Text(stringResource(R2.string.continue_as_guest))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WelcomeScreenPreview() {
    MaterialTheme {
        WelcomeScreen(
            uiState = WelcomeUiState(
                backdropUrl = null,
                movieTitle = "Popular Movie",
                isLoading = false,
            ),
            onLoginClick = {},
            onContinueAsGuestClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WelcomeScreenLoadingPreview() {
    MaterialTheme {
        WelcomeScreen(
            uiState = WelcomeUiState(isLoading = true),
            onLoginClick = {},
            onContinueAsGuestClick = {},
        )
    }
}
