package com.never.simplebtscanner.ui.saved_devices

import com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.BTDeviceDomain

sealed class SavedDevicesAction {
    data class AddDeviceToRepo(val btDevice: BTDeviceDomain) : SavedDevicesAction()
    data class RemoveDeviceFromRepo(val btDevice: BTDeviceDomain) : SavedDevicesAction()
}
