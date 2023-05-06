package com.never.simplebtscanner.ui.bt_scanner.utils.domain

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice

data class BluetoothDeviceDomain(
    val name: String?,
    val macAddress: String
) {
    companion object {
        @SuppressLint("MissingPermission")
        fun fromEntity(bluetoothDevice: BluetoothDevice) =
            BluetoothDeviceDomain(
                name = bluetoothDevice.name,
                macAddress = bluetoothDevice.address
            )
    }
}
