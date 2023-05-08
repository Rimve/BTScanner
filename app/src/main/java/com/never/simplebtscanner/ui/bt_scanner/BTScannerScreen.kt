package com.never.simplebtscanner.ui.bt_scanner

import android.Manifest
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.never.simplebtscanner.R
import com.never.simplebtscanner.ui.MainNavigationRoutes
import com.never.simplebtscanner.ui.bt_scanner.components.BTDeviceItemComponent
import com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.BTDeviceDomain
import com.never.simplebtscanner.utils.components.ScaffoldComponent
import com.never.simplebtscanner.utils.theme.AppTheme
import timber.log.Timber

@Composable
fun BTScannerScreen(
    navController: NavController,
    viewModel: BTScannerViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    SetupPermissionCheck(
        onPermissionsGranted = {
            viewModel.onAction(BTScannerAction.StartScanning)
        }
    )

    AppTheme {
        ScannerScreenContent(
            onAction = viewModel::onAction,
            state = state,
            onStopScan = {
                navController.navigate(
                    MainNavigationRoutes.SavedDevices.destination()
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScannerScreenContent(
    onAction: (BTScannerAction) -> Unit,
    state: BTScannerViewState,
    onStopScan: () -> Unit
) {
    ScaffoldComponent(
        title = stringResource(id = R.string.scan_devices_top_bar_label),
        onSearch = { onAction(BTScannerAction.OnSearchClick) }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    AnimatedVisibility(
                        visible = state.isSearching,
                        enter = slideInVertically { -it },
                        exit = slideOutVertically { -it }
                    ) {
                        TextField(
                            value = state.searchTerm,
                            onValueChange = { searchTerm ->
                                onAction(BTScannerAction.OnSearchTermUpdate(searchTerm))
                            },
                            placeholder = { SearchPlaceHolder() },
                            maxLines = 1,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                if (state.isSearching) {
                    items(
                        count = state.searchedDeviceList.size,
                        key = { state.searchedDeviceList[it].macAddress }
                    ) {
                        BTDeviceItemComponent(
                            btDeviceDomain = state.searchedDeviceList[it],
                            onSaveClick = { onSaveClick(state.searchedDeviceList[it], onAction) }
                        )
                    }
                } else {
                    items(
                        count = state.scannedDeviceList.size,
                        key = { state.scannedDeviceList[it].macAddress }
                    ) {
                        BTDeviceItemComponent(
                            btDeviceDomain = state.scannedDeviceList[it],
                            onSaveClick = { onSaveClick(state.scannedDeviceList[it], onAction) }
                        )
                    }
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
                Button(onClick = { onStopScan() }) {
                    Text(text = "Stop scanning")
                }
            }
        }
    }
}

@Composable
private fun SearchPlaceHolder() {
    Text(
        text = stringResource(id = R.string.scan_devices_search_field_placeholder_label)
    )
}

private fun onSaveClick(
    btDeviceDomain: BTDeviceDomain,
    onAction: (BTScannerAction) -> Unit
) {
    if (btDeviceDomain.isSaved) {
        onAction(
            BTScannerAction.RemoveDevice(btDeviceDomain)
        )
    } else {
        onAction(
            BTScannerAction.SaveDevice(btDeviceDomain)
        )
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
