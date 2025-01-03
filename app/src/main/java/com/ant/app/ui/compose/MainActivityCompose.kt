package com.ant.app.ui.compose

import BottomNavigationBar
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ant.analytics.AnalyticsHelper
import com.ant.app.ui.compose.app.MainApp
import com.ant.app.ui.compose.app.component.MoviesTopAppBar
import com.ant.app.ui.compose.app.rememberMoviesAppState
import com.ant.common.logger.TmdbLogger
import com.ant.design.theme.PopularMoviesTheme
import com.ant.ui.navigation.TopLevelDestination
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
        val appState = rememberMoviesAppState()
        PopularMoviesTheme {
            MainApp(appState)
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewBottomNavigationBar() {
        PopularMoviesTheme {
            val appState = rememberMoviesAppState()

            BottomNavigationBar(
                destinations = TopLevelDestination.entries,
                navController = appState.navController
            )
        }
    }
}