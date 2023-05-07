package com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.database

import com.never.simplebtscanner.data.AppDatabase
import com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.BTDeviceDomain
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BTDeviceLocalRepository @Inject constructor(private val database: AppDatabase) {
    fun insertBTDevice(btDevice: BTDeviceDomain) =
        database.btDeviceDao().insert(btDevice.toEntity())

    fun removeBTDevice(btDevice: BTDeviceDomain) =
        database.btDeviceDao().deleteByAddress(btDevice.macAddress)

    fun getBTDeviceList() = database.btDeviceDao().getAll().map { btDeviceEntityList ->
        btDeviceEntityList.map(BTDeviceEntity::toDomain)
    }

    fun deleteTable() = database.btDeviceDao().deleteAll()
}
