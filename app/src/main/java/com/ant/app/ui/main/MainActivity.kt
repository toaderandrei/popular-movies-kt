package com.ant.app.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.ant.analytics.AnalyticsEvent
import com.ant.analytics.AnalyticsHelper
import com.ant.app.R
import com.ant.app.databinding.ActivityMainBinding
import com.ant.app.ui.extensions.setInsets
import com.ant.app.ui.main.base.ToolbarWithNavigation
import com.ant.common.logger.TmdbLogger
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.ant.resources.R as R2

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ToolbarWithNavigation {
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var currentNavId = NAV_ID_NONE

    @Inject
    internal lateinit var logger: TmdbLogger

    @Inject
    internal lateinit var analyticsHelper: AnalyticsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_container
        ) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(TOP_LEVEL_DESTINATIONS)

        setupBottomNavigationBar()
        setInsets(binding)

        if (savedInstanceState == null) {
            currentNavId = navController.graph.startDestinationId
            val requestedNavId = intent.getIntExtra(EXTRA_NAVIGATION_ID, currentNavId)
            navigateTo(requestedNavId)
        }

        analyticsHelper.logEvent(
            AnalyticsEvent(
                type = AnalyticsEvent.Types.MAIN_SCREEN,
                mutableListOf(
                    AnalyticsEvent.Param(
                        AnalyticsEvent.ParamKeys.MAIN_SCREEN.name,
                        "onCreate"
                    )
                )
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        analyticsHelper.logEvent(
            AnalyticsEvent(
                type = AnalyticsEvent.Types.MAIN_SCREEN,
                mutableListOf(
                    AnalyticsEvent.Param(
                        AnalyticsEvent.ParamKeys.MAIN_SCREEN.name,
                        "onDestroy"
                    )
                )
            )
        )
    }

    override fun registerToolbarWithNavigation(toolbar: Toolbar) {
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    // =========================== private stuff =========================================================//

    private fun navigateTo(navId: Int) {
        if (navId == currentNavId) {
            return // user tapped the current item
        }
        navController.navigate(navId)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        binding.bottomNavigationView.apply {
            setupWithNavController(navController)
            addDestinationChangeListener()
            setOnItemReselectedListener { } // prevent navigating to the same item
        }
    }

    private fun addDestinationChangeListener() {
        navController.removeOnDestinationChangedListener(
            onDestinationChangedListener
        )

        // add onDestinationChangedListener to the new NavController
        navController.addOnDestinationChangedListener(onDestinationChangedListener)
    }

    private val onDestinationChangedListener =
        NavController.OnDestinationChangedListener { controller, destination, arguments ->
            logger.d("controller: $controller, destination: $destination, arguments: $arguments")
            currentNavId = destination.id
        }

    companion object {
        const val EXTRA_NAVIGATION_ID = "extra.NAVIGATION_ID"

        private const val NAV_ID_NONE = -1

        private val TOP_LEVEL_DESTINATIONS = setOf(
            R2.id.navigation_movies,
            R2.id.navigation_tvshow,
            R2.id.navigation_favorites,
            R2.id.navigation_account,
        )
    }
}