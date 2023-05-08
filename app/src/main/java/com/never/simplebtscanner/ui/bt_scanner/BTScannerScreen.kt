package com.never.simplebtscanner.ui.bt_scanner

import android.Manifest
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.never.simplebtscanner.R
import com.never.simplebtscanner.ui.bt_scanner.components.BTDeviceItemComponent
import com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.BTDeviceDomain
import com.never.simplebtscanner.utils.components.Dialog
import com.never.simplebtscanner.utils.components.ScaffoldComponent
import com.never.simplebtscanner.utils.theme.AppTheme
import timber.log.Timber

@Composable
fun BTScannerScreen(
    navController: NavController,
    viewModel: BTScannerViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val missingPermissionsMessage = stringResource(
        id = R.string.scan_devices_missing_permissions_snackbar_label
    )

    SetupPermissionCheck(
        onMissingPermissions = {
            viewModel.onAction(
                BTScannerAction.SetSnackbarMessage(missingPermissionsMessage)
            )
        },
        onPermissionsGranted = {
            viewModel.onAction(BTScannerAction.StartScanning)
        }
    )

    AppTheme {
        ScannerScreenContent(
            onAction = viewModel::onAction,
            state = state
        )
    }
}

@Composable
private fun ScannerScreenContent(
    onAction: (BTScannerAction) -> Unit,
    state: BTScannerViewState
) {
    ScaffoldComponent(
        title = stringResource(id = R.string.scan_devices_top_bar_label),
        onSearch = { onAction(BTScannerAction.OnSearchClick) },
        snackbarMessage = state.snackbarMessage,
        snackbarDismissed = { onAction(BTScannerAction.SetSnackbarMessage(null)) },
        bottomBar = {
            BottomButtonComponent(
                isVisible = state.isScanning,
                onStopScan = { onAction(BTScannerAction.StopScanning) },
                onStartScan = { onAction(BTScannerAction.StartScanning) }
            )
        }
    ) {
        if (state.isSearching && state.searchedDeviceList.isEmpty() && state.searchTerm.length > 1) {
            EmptySearchComponent(
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
        ) {
            SearchComponent(
                isVisible = state.isSearching,
                searchTerm = state.searchTerm,
                onValueChange = { searchTerm ->
                    onAction(BTScannerAction.OnSearchTermUpdate(searchTerm))
                }
            )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (state.isSearching) {
                    items(
                        count = state.searchedDeviceList.size,
                        key = { state.searchedDeviceList[it].macAddress }
                    ) {
                        BTDeviceItemComponent(
                            btDeviceDomain = state.searchedDeviceList[it],
                            onSaveClick = {
                                onDeviceSaveClick(
                                    state.searchedDeviceList[it],
                                    onAction
                                )
                            },
                            modifier = Modifier.clickable {
                                onAction(
                                    BTScannerAction.OnDeviceClick(
                                        state.searchedDeviceList[it]
                                    )
                                )
                            }
                        )
                    }
                } else {
                    items(
                        count = state.scannedDeviceList.size,
                        key = { state.scannedDeviceList[it].macAddress }
                    ) {
                        BTDeviceItemComponent(
                            btDeviceDomain = state.scannedDeviceList[it],
                            onSaveClick = {
                                onDeviceSaveClick(
                                    state.scannedDeviceList[it],
                                    onAction
                                )
                            },
                            modifier = Modifier.clickable {
                                onAction(
                                    BTScannerAction.OnDeviceClick(
                                        state.scannedDeviceList[it]
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }
    RenameDialogComponent(
        state = state,
        onAction = onAction
    )
}

@Composable
private fun SearchPlaceHolder() {
    Text(
        text = stringResource(id = R.string.scan_devices_search_field_placeholder_label)
    )
}

private fun onDeviceSaveClick(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchComponent(
    isVisible: Boolean,
    searchTerm: String,
    onValueChange: (String) -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically { -it },
        exit = slideOutVertically { -it }
    ) {
        TextField(
            value = searchTerm,
            onValueChange = { searchTerm ->
                onValueChange(searchTerm)
            },
            placeholder = { SearchPlaceHolder() },
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
    }
}

@Composable
private fun ScanningButtonComponent(onClick: () -> Unit, label: String) {
    Button(
        onClick = { onClick() },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = label)
    }
}

@Composable
private fun RenameDialogComponent(
    state: BTScannerViewState,
    onAction: (BTScannerAction) -> Unit
) {
    if (state.selectedDevice != null) {
        val renameSuccessMessage = stringResource(
            id = R.string.scan_devices_rename_device_success_snackbar_message
        )
        Dialog.WithTextField(
            title = stringResource(id = R.string.scan_devices_alert_rename_device_title),
            textFieldValue = state.selectedDeviceName,
            onValueChange = { searchTerm ->
                onAction(BTScannerAction.OnRenameDeviceTermUpdate(searchTerm))
            },
            onConfirm = {
                onAction(
                    BTScannerAction.OnRenameDevice(
                        nameTerm = state.selectedDeviceName,
                        btDevice = state.selectedDevice
                    )
                )
                onAction(
                    BTScannerAction.SetSnackbarMessage(message = renameSuccessMessage)
                )
            },
            confirmButtonLabel = stringResource(
                id = R.string.scan_devices_alert_rename_confirm_label
            ),
            onDismiss = { onAction(BTScannerAction.OnDeviceRenameDialogDismiss) },
            dismissButtonLabel = stringResource(
                id = R.string.scan_devices_alert_rename_cancel_label
            )
        )
    }
}

@Composable
private fun EmptySearchComponent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            painter = painterResource(id = R.drawable.ic_empty_result),
            contentDescription = "No search results found",
            modifier = Modifier.size(96.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.scan_devices_search_results_empty_message)
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun BottomButtonComponent(
    isVisible: Boolean,
    onStopScan: () -> Unit,
    onStartScan: () -> Unit
) {
    Box(modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)) {
        if (isVisible) {
            ScanningButtonComponent(
                onClick = { onStopScan() },
                label = stringResource(id = R.string.scan_devices_stop_scan_button_label)
            )
        } else {
            ScanningButtonComponent(
                onClick = { onStartScan() },
                label = stringResource(id = R.string.scan_devices_start_scan_button_label)
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun SetupPermissionCheck(
    onMissingPermissions: () -> Unit,
    onPermissionsGranted: () -> Unit
) {
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
            onMissingPermissions()
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
