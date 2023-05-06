package com.never.simplebtscanner.ui.bt_scanner

import com.never.simplebtscanner.ui.bt_scanner.utils.domain.BTDeviceDomain

data class BTScannerViewState(
    val scannedDeviceList: List<BTDeviceDomain> = listOf()
)
