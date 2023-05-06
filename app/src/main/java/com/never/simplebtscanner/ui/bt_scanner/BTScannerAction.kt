package com.never.simplebtscanner.ui.bt_scanner

import com.never.simplebtscanner.ui.bt_scanner.utils.domain.BTDeviceDomain

sealed class BTScannerAction {
    object StartScanning : BTScannerAction()
    object StopScanning : BTScannerAction()
    data class OnScannedResult(val scannedDeviceList: List<BTDeviceDomain>) : BTScannerAction()
}
