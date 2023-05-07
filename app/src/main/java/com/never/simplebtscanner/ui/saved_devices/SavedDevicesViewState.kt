package com.never.simplebtscanner.ui.saved_devices

import com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.BTDeviceDomain

data class SavedDevicesViewState(
    val savedDevices: List<BTDeviceDomain> = listOf()
)
