package com.never.simplebtscanner.ui.bt_scanner

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.never.simplebtscanner.ui.bt_scanner.utils.BTController
import com.never.simplebtscanner.ui.bt_scanner.utils.domain.BTDeviceDomain
import com.never.simplebtscanner.ui.theme.AppTheme
import timber.log.Timber

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScannerScreen(btController: BTController) {
    val permissionList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        listOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT
        )
    } else {
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.BLUETOOTH
        )
    }
    val scannedDeviceList by btController.scannedDeviceList.collectAsState()
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
        ScannerScreenContent(
            onStartScan = { btController.startDiscovery() },
            onStopScan = { btController.stopDiscovery() },
            scannedDevices = scannedDeviceList
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScannerScreenContent(
    onStartScan: () -> Unit,
    onStopScan: () -> Unit,
    scannedDevices: List<BTDeviceDomain>
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                items(
                    count = scannedDevices.size,
                    key = { scannedDevices[it].macAddress }
                ) {
                    Text(text = scannedDevices[it].macAddress)
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { onStartScan() }) {
                    Text(text = "Start scanning")
                }
                Button(onClick = { onStopScan() }) {
                    Text(text = "Stop scanning")
                }
            }
        }
    }
}
