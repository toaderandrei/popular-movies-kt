package com.ant.app.ui.compose.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.ant.app.ui.compose.app.component.MoviesTopAppBar
import com.ant.ui.navigation.TopLevelDestination
import com.ant.resources.R as R2

@Composable
fun MainApp(
    appState: MoviesAppState = rememberMoviesAppState(),
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val currentDestination = appState.currentDestination
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavigationSuiteScaffold(
            navigationSuiteItems = {
                appState.topLevelDestinations.forEach { destination ->
                    val selected = currentDestination
                        .isTopLevelDestinationInHierarchy(destination)
                    item(
                        selected = selected,
                        onClick = { appState.navigateToDestination(destination) },
                        icon = {
                            if (selected) {
                                Icon(
                                    imageVector = destination.selectedIcon,
                                    contentDescription = null,
                                )
                            } else {
                                Icon(
                                    imageVector = destination.unselectedIcon,
                                    contentDescription = null,
                                )
                            }
                        },
                        label = { stringResource(destination.titleTextId) }
                    )
                }
            },
            layoutType = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(
                windowAdaptiveInfo
            ),
        ) {
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
                topBar = {
                    if (appState.shouldShowTopAppBar) {
                        MoviesTopAppBar(
                            titleRes = appState.currentTopLevelDestination?.titleTextId
                                ?: R2.string.app_name,
                            onActionClick = appState::navigateToSearch,
                            actionIcon = Icons.Default.Search,
                            actionIconContentDescription = stringResource(R2.string.search_hint),
                        )
                    }
                }
            ) { padding ->
                AppNavHost(
                    appState = appState,
                    onShowSnackbar = { message, action ->
                        snackbarHostState.showSnackbar(
                            message = message,
                            actionLabel = action,
                            duration = SnackbarDuration.Short
                        ) == SnackbarResult.ActionPerformed
                    },
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}


private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false

