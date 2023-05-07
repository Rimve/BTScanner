package com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.BTDeviceDomain

@Entity(tableName = BTDeviceEntity.DB_TABLE_NAME)
data class BTDeviceEntity(
    @PrimaryKey
    val macAddress: String,
    val name: String?,
    val isSaved: Boolean
) {
    fun toDomain() = BTDeviceDomain(
        name = name,
        macAddress = macAddress,
        isSaved = isSaved
    )

    companion object {
        const val DB_TABLE_NAME = "bt_device"
    }
}
