package com.never.simplebtscanner.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.never.simplebtscanner.BuildConfig
import com.never.simplebtscanner.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber.DebugTree
import timber.log.Timber.Forest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
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
