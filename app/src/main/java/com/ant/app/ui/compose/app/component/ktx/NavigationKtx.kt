package com.ant.app.ui.compose.app.component.ktx

import androidx.compose.material3.adaptive.navigationsuite.ExperimentalMaterial3AdaptiveNavigationSuiteApi
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItemColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.ant.ui.navigation.Graph
import com.ant.ui.navigation.MainScreenDestination

//@OptIn(ExperimentalMaterial3AdaptiveNavigationSuiteApi::class)
//class NavigationSuiteScope internal constructor(
//    private val navigationSuiteScope: NavigationSuiteScope,
//    private val navigationSuiteItemColors: NavigationSuiteItemColors,
//) {
//    fun item(
//        selected: Boolean,
//        onClick: () -> Unit,
//        modifier: Modifier = Modifier,
//        icon: @Composable () -> Unit,
//        selectedIcon: @Composable () -> Unit = icon,
//        label: @Composable (() -> Unit)? = null,
//    ) = navigationSuiteScope.item(
//        selected = selected,
//        onClick = onClick,
//        icon = {
//            if (selected) {
//                selectedIcon()
//            } else {
//                icon()
//            }
//        },
//        label = label,
//        colors = navigationSuiteItemColors,
//        modifier = modifier,
//    )
//}

fun NavDestination?.isMainScreen(): Boolean {
    return this?.hierarchy
        ?.any { it.route == Graph.MAIN }
        ?: false
}


fun NavDestination?.isTopLevelDestinationInHierarchy(destination: MainScreenDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false

