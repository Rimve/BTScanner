package com.never.simplebtscanner.ui.bt_scanner.utils.usecases

import com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.BTDeviceDomain
import com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.database.BTDeviceLocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DevicesUseCase @Inject constructor(
    private val btDeviceLocalRepository: BTDeviceLocalRepository
) {
    fun getBTDeviceList(): Flow<List<BTDeviceDomain>> =
        btDeviceLocalRepository.getBTDeviceList()

    fun insertBTDeviceList(btDeviceList: List<BTDeviceDomain>) =
        btDeviceLocalRepository.insertBTDeviceList(btDeviceList)

    fun insertBTDevice(btDevice: BTDeviceDomain) =
        btDeviceLocalRepository.insertBTDevice(btDevice)

    fun searchDevice(searchTerm: String): List<BTDeviceDomain> =
        btDeviceLocalRepository.searchDevice(searchTerm)
}
