package com.ant.app.ui.compose

import BottomNavigationBar
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ant.analytics.AnalyticsHelper
import com.ant.app.ui.compose.app.MainApp
import com.ant.app.ui.compose.app.component.MoviesTopAppBar
import com.ant.app.ui.compose.app.rememberMainContentState
import com.ant.common.logger.TmdbLogger
import com.ant.design.theme.PopularMoviesTheme
import com.ant.ui.navigation.MainScreenDestination
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivityCompose : ComponentActivity() {
    @Inject
    internal lateinit var logger: TmdbLogger

    @Inject
    internal lateinit var analyticsHelper: AnalyticsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp()
        }
    }

    @Composable
    fun MyApp() {
        PopularMoviesTheme {
            MainApp()
        }
    }

    @Preview(
        name = "Movies Top App Bar Preview",
        showBackground = true,
        backgroundColor = 0xFFFFFF, // Optional: white background
        widthDp = 360 // Optional: set width
    )
    @Composable
    fun PreviewMoviesTopAppBar() {
        PopularMoviesTheme { // Apply your app's theme
            MoviesTopAppBar(
                titleRes = com.ant.resources.R.string.movies,
                actionIcon = Icons.Default.Settings,
                actionIconContentDescription = "Settings",
                onActionClick = {},
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewMainApp() {
        PopularMoviesTheme {
            MainApp()
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewBottomNavigationBar() {
        PopularMoviesTheme {
            val appState = rememberMainContentState()

            BottomNavigationBar(
                destinations = MainScreenDestination.entries,
                navController = appState.navController
            )
        }
    }
}