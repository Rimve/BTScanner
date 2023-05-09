package com.never.simplebtscanner.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.database.BTDeviceDao
import com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.database.BTDeviceEntity

@Database(entities = [BTDeviceEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun btDeviceDao(): BTDeviceDao
}

