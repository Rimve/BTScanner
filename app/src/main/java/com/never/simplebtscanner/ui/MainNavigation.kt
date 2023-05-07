package com.never.simplebtscanner.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.never.simplebtscanner.ui.bt_scanner.BTScannerScreen
import com.never.simplebtscanner.ui.saved_devices.SavedDeviceScreen

sealed class MainNavigationRoutes(val routePattern: String) {
    object BTScannerRoute : MainNavigationRoutes("btScanner") {
        val route: String get() = String.format(routePattern)
        fun destination(): String = String.format(routePattern)
    }

    object SavedDevices : MainNavigationRoutes("savedDevices") {
        val route: String get() = String.format(routePattern)
        fun destination(): String = String.format(routePattern)
    }
}

@Composable
fun MainNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = MainNavigationRoutes.BTScannerRoute.route
    ) {
        addBTScanner()
        addSavedDevices()
    }
}

private fun NavGraphBuilder.addBTScanner() {
    composable(route = MainNavigationRoutes.BTScannerRoute.route) {
        BTScannerScreen()
    }
}

private fun NavGraphBuilder.addSavedDevices() {
    composable(route = MainNavigationRoutes.SavedDevices.route) {
        SavedDeviceScreen()
    }
}
