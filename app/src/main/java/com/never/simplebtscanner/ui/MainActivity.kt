package com.never.simplebtscanner.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.never.simplebtscanner.BuildConfig
import com.never.simplebtscanner.ui.bt_scanner.BTScannerScreen
import com.never.simplebtscanner.ui.saved_devices.SavedDevicesScreen
import com.never.simplebtscanner.utils.components.AppBottomBar
import com.never.simplebtscanner.utils.components.ScaffoldComponent
import com.never.simplebtscanner.utils.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber.DebugTree
import timber.log.Timber.Forest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        if (BuildConfig.DEBUG) {
            Forest.plant(DebugTree())
        }

        setContent {
            val mainRouteList = listOf(
                MainNavigationRoutes.BTScannerRoute,
                MainNavigationRoutes.SavedDevices,
            )
            val coroutineScope = rememberCoroutineScope()
            val pagerState = rememberPagerState(0)

            AppTheme {
                ScaffoldComponent(
                    bottomBar = {
                        AppBottomBar.Pager(
                            pageList = mainRouteList,
                            onClick = { index ->
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            currentPage = mainRouteList[pagerState.currentPage]
                        )
                    }
                ) {
                    HorizontalPager(
                        pageCount = mainRouteList.size,
                        key = { mainRouteList[it].screenName },
                        state = pagerState
                    ) { pageIndex ->
                        when (mainRouteList[pageIndex]) {
                            MainNavigationRoutes.BTScannerRoute -> BTScannerScreen()
                            MainNavigationRoutes.SavedDevices -> SavedDevicesScreen()
                        }
                    }
                }
            }
        }
    }
}
