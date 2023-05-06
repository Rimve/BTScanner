package com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.BTDeviceDomain

@Entity(tableName = BTDeviceEntity.DB_TABLE_NAME)
data class BTDeviceEntity(
    @ColumnInfo(name = "name") val name: String?,
    @PrimaryKey
    @ColumnInfo(name = "mac_address") val macAddress: String
) {
    fun toDomain() = BTDeviceDomain(
        name = name,
        macAddress = macAddress
    )

    companion object {
        const val DB_TABLE_NAME = "bt_device"
    }
}
