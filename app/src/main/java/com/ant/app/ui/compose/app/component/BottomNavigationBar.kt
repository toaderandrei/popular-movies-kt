import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ant.ui.navigation.TopLevelDestination

@Composable
fun BottomNavigationBar(
    destinations: List<TopLevelDestination>,
    navController: NavController
) {
    val currentBackStack = navController.currentBackStackEntryAsState()

    NavigationBar {
        destinations.forEach { destination ->
            val selected = currentBackStack.value?.destination?.route == destination.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (currentBackStack.value?.destination?.route != destination.route) {
                        navController.navigate(destination) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (selected) destination.selectedIcon else destination.unselectedIcon,
                        contentDescription = stringResource(destination.titleTextId),
                    )
                },
                label = { Text(destination.route.replaceFirstChar { it.uppercase() }) }
            )
        }
    }
}