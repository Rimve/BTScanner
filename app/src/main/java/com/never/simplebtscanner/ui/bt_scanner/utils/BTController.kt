package com.never.simplebtscanner.ui.bt_scanner.utils

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.BTDeviceDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("MissingPermission")
class BTController @Inject constructor(private val context: Context) : BroadcastReceiver() {
    private val bluetoothAdapter by lazy {
        context.getSystemService(BluetoothManager::class.java)?.adapter
    }

    private val _mutableScannedDeviceList =
        MutableStateFlow<List<BTDeviceDomain>>(emptyList())
    val scannedDeviceList: StateFlow<List<BTDeviceDomain>>
        get() = _mutableScannedDeviceList.asStateFlow()

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            BluetoothDevice.ACTION_FOUND -> {
                val device = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(
                        BluetoothDevice.EXTRA_DEVICE,
                        BluetoothDevice::class.java
                    )
                } else {
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                }
                Timber.i("[BT Controller]: Device found")
                device?.let {
                    _mutableScannedDeviceList.update { devices ->
                        val newDevice = BTDeviceDomain.fromEntity(device)
                        if (newDevice in devices) devices else devices + newDevice
                    }
                }
            }
        }
    }

    fun startDiscovery() {
        if (!hasScanPermission()) {
            return
        }

        context.registerReceiver(this, IntentFilter(BluetoothDevice.ACTION_FOUND))
        bluetoothAdapter?.startDiscovery()
    }

    fun stopDiscovery() {
        if (!hasScanPermission()) {
            return
        }

        bluetoothAdapter?.cancelDiscovery()
    }

    fun release() {
        context.unregisterReceiver(this)
    }

    private fun hasScanPermission(): Boolean {
        val isGranted = PackageManager.PERMISSION_GRANTED
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            context.checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) == isGranted
        } else {
            context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == isGranted &&
                    context.checkSelfPermission(Manifest.permission.BLUETOOTH) == isGranted
        }
    }
}
