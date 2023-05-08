package com.never.simplebtscanner.ui.bt_scanner

import com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.BTDeviceDomain

sealed class BTScannerAction {
    object StartScanning : BTScannerAction()
    object StopScanning : BTScannerAction()
    object OnSearchClick : BTScannerAction()
    object OnDeviceRenameDialogDismiss : BTScannerAction()
    data class OnDeviceClick(val btDevice: BTDeviceDomain) : BTScannerAction()
    data class SaveDevice(val btDevice: BTDeviceDomain) : BTScannerAction()
    data class RemoveDevice(val btDevice: BTDeviceDomain) : BTScannerAction()
    data class OnSearchTermUpdate(val searchTerm: String) : BTScannerAction()
    data class OnRenameDeviceTermUpdate(val nameTerm: String) : BTScannerAction()
}
