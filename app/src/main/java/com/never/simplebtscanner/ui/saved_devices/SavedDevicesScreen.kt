package com.never.simplebtscanner.ui.saved_devices

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.never.simplebtscanner.ui.theme.AppTheme
import com.never.simplebtscanner.utils.components.AppTopBar

@Composable
fun ScannedDeviceScreen() {
    AppTheme {
        ScannedDeviceContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScannedDeviceContent() {
    Scaffold(
        topBar = { AppTopBar.Primary(title = "Scanned devices") }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
        }
    }
}
