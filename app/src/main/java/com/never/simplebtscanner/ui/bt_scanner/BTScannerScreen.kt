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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.never.simplebtscanner.ui.bt_scanner.components.BTDeviceItemComponent
import com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.BTDeviceDomain
import com.never.simplebtscanner.ui.theme.AppTheme
import timber.log.Timber

@Composable
fun BTScannerScreen(viewModel: BTScannerViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    SetupPermissionCheck(
        onPermissionsGranted = {
            viewModel.onAction(BTScannerAction.StartScanning)
        }
    )

    AppTheme {
        ScannerScreenContent(
            onAction = viewModel::onAction,
            scannedDevices = state.scannedDeviceList
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScannerScreenContent(
    onAction: (BTScannerAction) -> Unit,
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
                    BTDeviceItemComponent(scannedDevices[it])
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { onAction(BTScannerAction.StartScanning) }) {
                    Text(text = "Start scanning")
                }
                Button(onClick = { onAction(BTScannerAction.StopScanning) }) {
                    Text(text = "Stop scanning")
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun SetupPermissionCheck(onPermissionsGranted: () -> Unit) {
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

    LaunchedEffect(permissionState.allPermissionsGranted) {
        if (permissionState.allPermissionsGranted) {
            onPermissionsGranted()
        }
    }
}
