package com.ant.app.ui.compose.themes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ant.resources.R as R2
import com.ant.app.ui.compose.app.component.LocalBackgroundTheme

@Composable
fun PopularMoviesBackground(
    modifier: Modifier = Modifier, content: @Composable () -> Unit
) {
    val color = LocalBackgroundTheme.current.color
    val tonalElevation = LocalBackgroundTheme.current.tonalElevation
    Surface(
        color = if (color == Color.Unspecified) Color.Transparent else color,
        tonalElevation = if (tonalElevation == Dp.Unspecified) 0.dp else tonalElevation,
        modifier = modifier.fillMaxSize(),
    ) {
        CompositionLocalProvider(LocalAbsoluteTonalElevation provides 0.dp) {
            content()
        }
    }
}

@Composable
fun PopularMoviesGradientBackground(
    modifier: Modifier = Modifier,
    gradientColors: GradientColors = LocalGradientColors.current,
    content: @Composable () -> Unit
) {
    Surface(
        color = if (gradientColors.container == Color.Unspecified) {
            Color.Transparent
        } else {
            gradientColors.container
        },
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            Modifier.fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            gradientColors.top,
                            gradientColors.bottom
                        )
                    )
                )
        ) {
            content()
        }
    }
}

/**
 * A class to model gradient color values for Now in Android.
 *
 * @param top The top gradient color to be rendered.
 * @param bottom The bottom gradient color to be rendered.
 * @param container The container gradient color over which the gradient will be rendered.
 */
@Immutable
data class GradientColors(
    val top: Color = Color(0xFFE3F2FD), // Very Light Blue
    val bottom: Color = Color(0xFFBBDEFB), // Light Blue
    val container: Color = Color.Unspecified,
)
/**
 * A composition local for [GradientColors].
 */
val LocalGradientColors = staticCompositionLocalOf { GradientColors() }
