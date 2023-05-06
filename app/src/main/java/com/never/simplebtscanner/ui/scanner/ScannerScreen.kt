package com.never.simplebtscanner.ui.scanner

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.never.simplebtscanner.ui.theme.AppTheme
import timber.log.Timber

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScannerScreen() {
    val permissionList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        listOf(
            Manifest.permission.BLUETOOTH_SCAN
        )
    } else {
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.BLUETOOTH
        )
    }
    val permissionState = rememberMultiplePermissionsState(
        permissions = permissionList
    ) { permissionMap ->
        val isMissingPermissions = permissionMap.any { !it.value }
        if (isMissingPermissions) {
            Timber.i("[ScannerScreen] Missing permissions")
        } else {
            Timber.i("[ScannerScreen] Permissions granted")
        }
    }

    SideEffect {
        permissionState.launchMultiplePermissionRequest()
    }

    AppTheme {
        ScannerScreenContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScannerScreenContent() {
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(text = "SCANNER", modifier = Modifier.align(Alignment.Center))
        }
    }
}
