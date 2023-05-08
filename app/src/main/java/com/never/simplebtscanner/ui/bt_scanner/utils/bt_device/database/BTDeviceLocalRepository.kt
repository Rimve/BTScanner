package com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.database

import com.never.simplebtscanner.data.AppDatabase
import com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.BTDeviceDomain
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BTDeviceLocalRepository @Inject constructor(private val database: AppDatabase) {
    fun insertBTDeviceList(btDeviceList: List<BTDeviceDomain>) =
        database.btDeviceDao().insertList(btDeviceList.map(BTDeviceDomain::toEntity))

    fun insertBTDevice(btDevice: BTDeviceDomain) =
        database.btDeviceDao().insert(
            btDevice.toEntity()
        )

    fun removeBTDevice(btDevice: BTDeviceDomain) =
        database.btDeviceDao().deleteByAddress(btDevice.macAddress)

    fun getBTDeviceList() = database.btDeviceDao().getAll().map { btDeviceEntityList ->
        btDeviceEntityList.map(BTDeviceEntity::toDomain).sortedBy { it.macAddress }
    }

    fun getSavedDeviceList() = database.btDeviceDao().getAllSaved().map { btDeviceEntityList ->
        btDeviceEntityList.map(BTDeviceEntity::toDomain).sortedBy { it.macAddress }
    }
}
