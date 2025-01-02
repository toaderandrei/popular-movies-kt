package com.ant.app.ui.compose

import BottomNavigationBar
import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ant.analytics.AnalyticsHelper
import com.ant.app.R
import com.ant.app.ui.compose.app.MainApp
import com.ant.app.ui.compose.app.component.MoviesTopAppBar
import com.ant.app.ui.compose.app.rememberMoviesAppState
import com.ant.common.logger.TmdbLogger
import com.ant.design.theme.PopularMoviesTheme
import com.ant.ui.navigation.TopLevelDestination
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity2 : AppCompatActivity() {
    @Inject
    internal lateinit var logger: TmdbLogger

    @Inject
    internal lateinit var analyticsHelper: AnalyticsHelper

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
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
                navigationIcon = Icons.Default.Menu,
                navigationIconContentDescription = "Menu",
                actionIcon = Icons.Default.Settings,
                actionIconContentDescription = "Settings",
                onActionClick = {},
                onNavigationClick = {}
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewMainApp() {
        val appState = rememberMoviesAppState()
        PopularMoviesTheme {
            MainApp(appState = appState)
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

    @Preview(
        name = "Light Mode",
        showBackground = true
    )
    @Preview(
        name = "Dark Mode",
        showBackground = true,
        uiMode = Configuration.UI_MODE_NIGHT_YES
    )
    @Composable
    fun PreviewMoviesTopAppBarDarkLight() {
        PopularMoviesTheme {
            MoviesTopAppBar(
                titleRes = com.ant.resources.R.string.movies,
                navigationIcon = Icons.Default.Menu,
                navigationIconContentDescription = "Menu",
                actionIcon = Icons.Default.Settings,
                actionIconContentDescription = "Settings",
                onActionClick = {},
                onNavigationClick = {}
            )
        }
    }
}