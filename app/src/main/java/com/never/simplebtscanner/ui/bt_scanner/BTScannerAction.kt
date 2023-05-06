package com.never.simplebtscanner.ui.bt_scanner

sealed class BTScannerAction {
    object OnStartScanning : BTScannerAction()
    object OnStopScanning : BTScannerAction()
}
