package com.never.simplebtscanner.ui.bt_scanner

sealed class BTScannerAction {
    object StartScanning : BTScannerAction()
    object StopScanning : BTScannerAction()
}
