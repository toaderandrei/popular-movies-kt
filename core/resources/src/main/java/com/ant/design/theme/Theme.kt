package com.ant.design.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

private val LightColors = androidx.compose.material3.lightColorScheme(
    primary = primaryColor,
    primaryContainer = primaryVariantColor,
    secondary = secondaryColor,
    background = backgroundColor,
    surface = surfaceColor,
    error = errorColor,
    onPrimary = onPrimaryColor,
    onSecondary = onSecondaryColor,
    onBackground = onBackgroundColor,
    onSurface = onSurfaceColor,
    onError = onErrorColor
)

private val DarkColors = androidx.compose.material3.darkColorScheme(
    primary = primaryDarkColor,
    primaryContainer = primaryVariantDarkColor,
    secondary = secondaryDarkColor,
    background = backgroundDarkColor,
    surface = surfaceDarkColor,
    error = errorDarkColor,
    onPrimary = onPrimaryDarkColor,
    onSecondary = onSecondaryDarkColor,
    onBackground = onBackgroundDarkColor,
    onSurface = onSurfaceDarkColor,
    onError = onErrorDarkColor
)

@Composable
fun PopularMoviesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = JetcasterTypography,
        content = content
    )
}
