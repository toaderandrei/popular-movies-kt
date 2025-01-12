package com.ant.app.ui.compose.app

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.ant.app.ui.compose.app.component.ktx.PopularMoviesNavigationSuiteScaffold
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
import com.ant.resources.R as R2

@Composable
fun MainApp(
    navController: NavHostController = rememberNavController(),
    mainActivityViewModel: MainActivityViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val authenticationState by mainActivityViewModel.isUserLoggedIn.collectAsStateWithLifecycle()
    var isLoading by remember { mutableStateOf(true) }
    val authenticationStateTitle = stringResource(R2.string.checking_authentication_state)

    LaunchedEffect(Unit) {
        // Add an artificial delay for loading simulation
        snackbarHostState.showSnackbar(authenticationStateTitle)
        isLoading = false
    }

    // TODO verify if we can get rid of scaffold without removing snackbar when checking for authentication.
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->

        when {
            isLoading -> {
                LoadingScreen()
            }

            else -> {
                PopularMoviesBackground {
                    PopularMoviesGradientBackground(gradientColors = GradientColors()) {
                        // Single NavHost controlling both auth & main flows
                        NavHost(
                            navController = navController,
                            startDestination = if (authenticationState == true) {
                                Graph.MAIN
                            } else {
                                Graph.AUTHENTICATION
                            },
                            route = Graph.ROOT,
                        ) {
                            // Nested graph for authentication
                            authGraph(navController)

                            // Nested graph for main flow
                            composable(Graph.MAIN) {
                                MainContent(
                                    outerPadding = paddingValues,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun MainContent(
    outerPadding: PaddingValues,
    mainContentState: MainContentState = rememberMainContentState(),
) {
    val mainNavController: NavHostController = mainContentState.navController
    // Observe the current nav route for highlighting
    val currentDestination = mainContentState.currentDestination
    // Identify window size & adapt. Could pass your own windowAdaptiveInfo if desired
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()

    PopularMoviesNavigationSuiteScaffold(
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
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(16.dp) // Optional padding for spacing

                        ) {
                            Icon(
                                imageVector = if (!selected) destination.unselectedIcon else destination.selectedIcon,
                                contentDescription = null,
                            )
                            Text(
                                text = stringResource(destination.titleTextId),
                                style = MaterialTheme.typography.labelSmall,
                                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                )
            }
        },
        windowAdaptiveInfo = windowAdaptiveInfo,
        modifier = Modifier
            .consumeWindowInsets(outerPadding)
            .padding(outerPadding)
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
        route = Graph.AUTHENTICATION,
    ) {
        loginGraph(navController)
    }
}