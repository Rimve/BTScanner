package com.never.simplebtscanner.ui.saved_devices.utils.usecases

import com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.BTDeviceDomain
import com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.database.BTDeviceLocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SavedDevicesUseCase @Inject constructor(
    private val btDeviceLocalRepository: BTDeviceLocalRepository
) {
    fun getSavedDeviceList(): Flow<List<BTDeviceDomain>> =
        btDeviceLocalRepository.getSavedDeviceList()

    fun insertSavedBTDevice(btDevice: BTDeviceDomain) =
        btDeviceLocalRepository.insertSavedBTDevice(btDevice)

    fun removeSavedBTDevice(btDevice: BTDeviceDomain) =
        btDeviceLocalRepository.removeSavedBTDevice(btDevice)
}
