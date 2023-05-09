package com.never.simplebtscanner.utils.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.never.simplebtscanner.ui.MainNavigationRoutes

object AppBottomBar {
    @Composable
    fun Navigation(navController: NavController, items: List<MainNavigationRoutes>) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            items.forEach { screen ->
                NavigationBarItem(
                    alwaysShowLabel = false,
                    icon = {
                        Icon(
                            painter = painterResource(id = screen.iconResId),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    colors = navigationBarColors(),
                    label = { Text(text = screen.screenName) },
                    selected = currentDestination?.hierarchy?.any { it.route == screen.routePattern } == true,
                    onClick = {
                        navController.navigate(screen.routePattern) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }

    @Composable
    fun Pager(
        pageList: List<MainNavigationRoutes>,
        currentPage: MainNavigationRoutes,
        onClick: (Int) -> Unit
    ) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            pageList.forEachIndexed { index, screen ->
                NavigationBarItem(
                    alwaysShowLabel = false,
                    icon = {
                        Icon(
                            painter = painterResource(id = screen.iconResId),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    colors = navigationBarColors(),
                    label = { Text(text = screen.screenName) },
                    selected = screen == currentPage,
                    onClick = { onClick(index) }
                )
            }
        }
    }

    @Composable
    private fun navigationBarColors() = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.primary,
        selectedTextColor = MaterialTheme.colorScheme.onPrimary,
        indicatorColor = MaterialTheme.colorScheme.onPrimary,
        unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
        unselectedTextColor = MaterialTheme.colorScheme.onPrimary
    )
}
