package com.ant.app.ui.compose.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.ant.app.ui.compose.app.component.ktx.NavigationSuiteScaffold
import com.ant.app.ui.compose.app.component.ktx.isTopLevelDestinationInHierarchy
import com.ant.app.ui.compose.app.viewmodel.MainActivityViewModel
import com.ant.app.ui.compose.themes.GradientColors
import com.ant.app.ui.compose.themes.PopularMoviesBackground
import com.ant.app.ui.compose.themes.PopularMoviesGradientBackground
import com.ant.feature.login.navigation.loginGraph
import com.ant.feature.movies.navigation.moviesNavigation
import com.ant.feature.tvshow.navigation.tvShowNavigation
import com.ant.ui.navigation.Graph
import com.ant.ui.navigation.LoginScreenDestination
import com.ant.ui.navigation.MainScreenDestination

@Composable
fun MainApp(
    navController: NavHostController = rememberNavController(),
    mainActivityViewModel: MainActivityViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val authenticationState by mainActivityViewModel.authenticationStateStateFlow.collectAsState()

    PopularMoviesBackground(
        modifier = Modifier
    ) {
        PopularMoviesGradientBackground(
            gradientColors = GradientColors()
        ) {
            NavHost(
                navController = navController,
                route = Graph.ROOT,
                startDestination = if (authenticationState) Graph.MAIN else Graph.AUTHENTICATION,
            ) {
                authGraph(navController = navController)
                composable(Graph.MAIN) {
                    MainContent(snackbarHostState = snackbarHostState)
                }
            }
        }
    }
}

@Composable
fun MainContent(
    mainContentState: MainContentState = rememberMainContentState(),
    snackbarHostState: SnackbarHostState,
) {
    val mainNavController: NavHostController = mainContentState.navController
    // Observe the current nav route for highlighting
    val currentDestination = mainContentState.currentDestination
    // Identify window size & adapt. Could pass your own windowAdaptiveInfo if desired
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            mainContentState.topLevelDestinations.forEach { destination ->
                val selected = currentDestination
                    .isTopLevelDestinationInHierarchy(destination)
                item(
                    selected = selected,
                    onClick = {
                        mainContentState.navigateToDestination(destination)
                    },
                    icon = {
                        if (!selected) {
                            Icon(
                                imageVector = destination.unselectedIcon,
                                contentDescription = null,
                            )
                        } else {
                            Icon(
                                imageVector = destination.selectedIcon,
                                contentDescription = stringResource(destination.titleTextId),
                            )
                        }
                    },
                )
            }
        },
        windowAdaptiveInfo = windowAdaptiveInfo
    ) {
        Scaffold { paddingValues ->
            NavHost(
                navController = mainNavController,
                startDestination = MainScreenDestination.MOVIES.route,
                modifier = Modifier.padding(paddingValues)
            ) {
                moviesNavigation(mainNavController)
                tvShowNavigation(mainNavController)
            }
        }
    }
}

fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation(
        startDestination = LoginScreenDestination.LOGIN.route,
        route = Graph.AUTHENTICATION
    ) {
        loginGraph(navController)
    }
}