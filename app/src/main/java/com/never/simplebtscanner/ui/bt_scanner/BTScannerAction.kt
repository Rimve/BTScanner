package com.never.simplebtscanner.ui.bt_scanner

import com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.BTDeviceDomain

sealed class BTScannerAction {
    object StartScanning : BTScannerAction()
    object StopScanning : BTScannerAction()
    object OnSearchClick : BTScannerAction()
    data class AddDeviceToRepo(val btDevice: BTDeviceDomain) : BTScannerAction()
    data class RemoveDeviceFromRepo(val btDevice: BTDeviceDomain) : BTScannerAction()
    data class OnSearchTermUpdate(val searchTerm: String) : BTScannerAction()
}
