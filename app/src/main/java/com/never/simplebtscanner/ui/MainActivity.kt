package com.never.simplebtscanner.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.never.simplebtscanner.BuildConfig
import com.never.simplebtscanner.ui.bt_scanner.BTScannerScreen
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
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BTScannerScreen()
                }
            }
        }
    }
}
