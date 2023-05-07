package com.never.simplebtscanner.ui.bt_scanner

import com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.BTDeviceDomain

data class BTScannerViewState(
    val scannedDeviceList: List<BTDeviceDomain> = listOf(),
    val searchedDeviceList: List<BTDeviceDomain> = listOf(),
    val isSearching: Boolean = false,
    val searchTerm: String = ""
)
