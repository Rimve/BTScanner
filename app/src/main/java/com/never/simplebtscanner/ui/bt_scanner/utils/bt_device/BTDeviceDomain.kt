package com.never.simplebtscanner.ui.bt_scanner.utils.bt_device

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.database.BTDeviceEntity

data class BTDeviceDomain(
    val name: String?,
    val macAddress: String
) {
    fun toEntity() = BTDeviceEntity(
        name = name,
        macAddress = macAddress
    )

    companion object {
        @SuppressLint("MissingPermission")
        fun fromEntity(bluetoothDevice: BluetoothDevice) =
            BTDeviceDomain(
                name = bluetoothDevice.name,
                macAddress = bluetoothDevice.address
            )
    }
}
