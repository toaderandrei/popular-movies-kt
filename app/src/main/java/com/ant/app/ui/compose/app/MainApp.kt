package com.ant.app.ui.compose.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.rememberNavController
import com.ant.ui.navigation.TopLevelDestination
import com.ant.design.theme.PopularMoviesTheme
import com.ant.resources.R as R2

@Composable
fun MainApp(
    appState: MoviesAppState,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    PopularMoviesTheme {
        Scaffold(modifier = modifier,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                MoviesTopAppBar(
                    titleRes = appState.currentTopLevelDestination?.titleTextId
                        ?: R2.string.app_name,
                    navigationIcon = Icons.Default.Search,
                    navigationIconContentDescription = stringResource(R2.string.movie_search),
                    onActionClick = {},
                    onNavigationClick = {
                        appState.navigateToSearch()
                    },
                    actionIcon = Icons.Default.Settings,
                    actionIconContentDescription = stringResource(R2.string.settings),
                    modifier = modifier,
                )
            },
            bottomBar = {
                MoviesBottomBar(
                    destinations = com.ant.ui.navigation.TopLevelDestination.entries,
                    onNavigateToDestination = { appState.navigateToDestination(it) },
                    currentDestination = appState.currentDestination,
                    modifier = modifier
                )
            }) { padding ->
            AppNavHost(
                appState = appState,
                onShowSnackbar = { message, action ->
                    snackbarHostState.showSnackbar(
                        message = message, actionLabel = action, duration = SnackbarDuration.Short
                    ) == SnackbarResult.ActionPerformed
                },
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesTopAppBar(
    titleRes: Int,
    navigationIcon: ImageVector,
    navigationIconContentDescription: String,
    actionIcon: ImageVector,
    actionIconContentDescription: String,
    onActionClick: () -> Unit,
    onNavigationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(title = { Text(stringResource(titleRes)) }, navigationIcon = {
        IconButton(onClick = onNavigationClick) {
            Icon(
                imageVector = navigationIcon,
                contentDescription = navigationIconContentDescription
            )
        }
    }, actions = {
        IconButton(onClick = onActionClick) {
            Icon(
                imageVector = actionIcon, contentDescription = actionIconContentDescription
            )
        }
    }, colors = TopAppBarDefaults.topAppBarColors(
        containerColor = Color.Transparent
    ), modifier = modifier
    )
}

@Composable
fun MoviesBottomBar(
    destinations: List<com.ant.ui.navigation.TopLevelDestination>,
    onNavigateToDestination: (com.ant.ui.navigation.TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (selected) destination.selectedIcon else destination.unselectedIcon,
                        contentDescription = stringResource(destination.titleTextId),
                    )
                },
                label = { Text(stringResource(destination.titleTextId)) },
                selected = selected,
                onClick = { onNavigateToDestination(destination) })
        }
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: com.ant.ui.navigation.TopLevelDestination) =
    this?.hierarchy?.any { it.route?.contains(destination.name, true) ?: false } ?: false