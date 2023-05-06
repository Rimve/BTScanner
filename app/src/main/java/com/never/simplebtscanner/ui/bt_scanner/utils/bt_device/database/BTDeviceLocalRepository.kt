package com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.database

import com.never.simplebtscanner.data.AppDatabase
import com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.BTDeviceDomain
import javax.inject.Inject

class BTDeviceLocalRepository @Inject constructor(private val database: AppDatabase) {
    fun insertBTDevice(btDevice: BTDeviceDomain) {
        database.btDeviceDao.insert(btDevice.toEntity())
    }

    fun getBTDeviceList() = database.btDeviceDao.getAll().map(BTDeviceEntity::toDomain)
}
