package com.never.simplebtscanner.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.never.simplebtscanner.BuildConfig
import com.never.simplebtscanner.utils.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber.DebugTree
import timber.log.Timber.Forest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        if (BuildConfig.DEBUG) {
            Forest.plant(DebugTree())
        }

        setContent {
            AppTheme {
                MainNavigation()
            }
        }
    }
}
