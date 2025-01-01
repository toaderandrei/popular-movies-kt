package com.ant.app.ui.compose

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ant.analytics.AnalyticsHelper
import com.ant.app.ui.compose.app.MainApp
import com.ant.app.ui.compose.app.rememberMoviesAppState
import com.ant.common.logger.TmdbLogger
import com.ant.design.theme.PopularMoviesTheme
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
            val appState = rememberMoviesAppState()
            MainApp(appState = appState)
        }
    }

    @Composable
    @Preview
    fun RunAppPreview() {
        MyApp()
    }
}