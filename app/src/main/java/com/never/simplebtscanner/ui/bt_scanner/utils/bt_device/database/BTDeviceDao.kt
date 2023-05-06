package com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BTDeviceDao {
    @Query("SELECT * FROM ${BTDeviceEntity.DB_TABLE_NAME}")
    fun getAll(): List<BTDeviceEntity>

    @Query(
        "SELECT * FROM ${BTDeviceEntity.DB_TABLE_NAME} WHERE name LIKE :name" +
                " OR mac_address LIKE :macAddress LIMIT 1"
    )
    fun findByName(name: String?, macAddress: String): BTDeviceEntity

    @Insert
    fun insertAll(vararg users: BTDeviceEntity)

    @Delete
    fun delete(user: BTDeviceEntity)
}
