package com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.database

import com.never.simplebtscanner.data.AppDatabase
import com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.BTDeviceDomain
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BTDeviceLocalRepository @Inject constructor(private val database: AppDatabase) {
    fun insertBTDevices(btDeviceList: List<BTDeviceDomain>) =
        database.btDeviceDao().insertAll(btDeviceList.map(BTDeviceDomain::toEntity))

    fun getBTDeviceList() = database.btDeviceDao().getAll().map { btDeviceEntityList ->
        btDeviceEntityList.map(BTDeviceEntity::toDomain)
    }
}
