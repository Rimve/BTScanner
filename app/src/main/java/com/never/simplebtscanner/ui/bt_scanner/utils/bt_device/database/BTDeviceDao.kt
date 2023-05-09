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

    @Query("SELECT * FROM ${BTDeviceEntity.DB_TABLE_NAME} WHERE isSaved LIKE 1")
    fun getAllSaved(): Flow<List<BTDeviceEntity>>

    @Query(
        "SELECT * FROM ${BTDeviceEntity.DB_TABLE_NAME} WHERE name LIKE '%' || :searchTerm || '%'" +
                " OR macAddress LIKE '%' || :searchTerm || '%'"
    )
    fun findByNameOrMacAddress(searchTerm: String): List<BTDeviceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(btDevice: BTDeviceEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertList(btDeviceList: List<BTDeviceEntity>)
}
