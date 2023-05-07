package com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BTDeviceDao {
    @Query("SELECT * FROM ${BTDeviceEntity.DB_TABLE_NAME}")
    fun getAll(): Flow<List<BTDeviceEntity>>

    @Query(
        "SELECT * FROM ${BTDeviceEntity.DB_TABLE_NAME} WHERE name LIKE :name" +
                " OR macAddress LIKE :macAddress LIMIT 1"
    )
    fun findByNameOrMacAddress(name: String?, macAddress: String): BTDeviceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(btDevice: BTDeviceEntity)

    @Query("DELETE FROM ${BTDeviceEntity.DB_TABLE_NAME} WHERE macAddress = :macAddress")
    fun deleteByAddress(macAddress: String)

    @Query("DELETE FROM ${BTDeviceEntity.DB_TABLE_NAME}")
    fun deleteAll()
}
