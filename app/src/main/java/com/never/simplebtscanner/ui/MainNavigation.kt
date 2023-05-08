package com.never.simplebtscanner.ui

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.never.simplebtscanner.R
import com.never.simplebtscanner.ui.bt_scanner.BTScannerScreen
import com.never.simplebtscanner.ui.saved_devices.SavedDevicesScreen

sealed class MainNavigationRoutes(
    val routePattern: String,
    @DrawableRes val iconResId: Int,
    val screenName: String
) {
    object BTScannerRoute : MainNavigationRoutes(
        routePattern = "btScanner",
        iconResId = R.drawable.ic_bluetooth,
        screenName = "Scanner"
    ) {
        val route: String get() = String.format(routePattern)
    }

    object SavedDevices : MainNavigationRoutes(
        routePattern = "savedDevices",
        iconResId = R.drawable.ic_favorite_filled,
        screenName = "Saved"
    ) {
        val route: String get() = String.format(routePattern)
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
        SavedDevicesScreen()
    }
}
