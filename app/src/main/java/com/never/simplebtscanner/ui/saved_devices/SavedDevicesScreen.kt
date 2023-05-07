package com.never.simplebtscanner.ui.saved_devices

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.never.simplebtscanner.R
import com.never.simplebtscanner.ui.bt_scanner.components.BTDeviceItemComponent
import com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.BTDeviceDomain
import com.never.simplebtscanner.utils.components.ScaffoldComponent
import com.never.simplebtscanner.utils.theme.AppTheme

@Composable
fun SavedDevicesScreen(
    navController: NavController,
    viewModel: SavedDevicesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    AppTheme {
        SavedDevicesContent(
            savedDeviceList = state.savedDevices,
            onAction = viewModel::onAction,
            onBack = navController::popBackStack
        )
    }
}

@Composable
private fun SavedDevicesContent(
    savedDeviceList: List<BTDeviceDomain>,
    onAction: (SavedDevicesAction) -> Unit,
    onBack: () -> Unit
) {
    ScaffoldComponent(
        title = stringResource(id = R.string.saved_devices_top_bar_label),
        onBack = { onBack() }
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                count = savedDeviceList.size,
                key = { savedDeviceList[it].macAddress }
            ) {
                BTDeviceItemComponent(
                    btDeviceDomain = savedDeviceList[it],
                    onSaveClick = {
                        if (savedDeviceList[it].isSaved) {
                            onAction(SavedDevicesAction.RemoveDeviceFromRepo(savedDeviceList[it]))
                        } else {
                            onAction(SavedDevicesAction.AddDeviceToRepo(savedDeviceList[it]))
                        }
                    }
                )
            }
        }
    }
}
