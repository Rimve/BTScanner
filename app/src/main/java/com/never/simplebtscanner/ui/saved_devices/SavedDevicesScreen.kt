package com.never.simplebtscanner.ui.saved_devices

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.never.simplebtscanner.R
import com.never.simplebtscanner.ui.theme.AppTheme
import com.never.simplebtscanner.utils.components.ScaffoldComponent

@Composable
fun ScannedDeviceScreen() {
    AppTheme {
        ScannedDeviceContent()
    }
}

@Composable
private fun ScannedDeviceContent() {
    ScaffoldComponent(title = stringResource(id = R.string.saved_devices_top_bar_label)) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
        }
    }
}
