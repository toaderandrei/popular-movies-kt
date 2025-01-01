import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(
    destinations: List<String>,
    navController: NavController
) {
    val currentBackStack = navController.currentBackStackEntryAsState()

    NavigationBar {
        destinations.forEach { destination ->
            NavigationBarItem(
                selected = currentBackStack.value?.destination?.route == destination,
                onClick = {
                    if (currentBackStack.value?.destination?.route != destination) {
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
                    // Replace with actual icons for your destinations
                    Icon(imageVector = Icons.Default.Home, contentDescription = null)
                },
                label = { Text(destination.replaceFirstChar { it.uppercase() }) }
            )
        }
    }
}