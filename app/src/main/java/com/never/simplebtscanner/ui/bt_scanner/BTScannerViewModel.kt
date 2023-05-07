package com.never.simplebtscanner.ui.bt_scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.never.simplebtscanner.di.IoDispatcher
import com.never.simplebtscanner.ui.bt_scanner.utils.BTController
import com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.BTDeviceDomain
import com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.database.BTDeviceLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BTScannerViewModel @Inject constructor(
    private val btController: BTController,
    private val btDeviceLocalRepository: BTDeviceLocalRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _state = MutableStateFlow(BTScannerViewState())
    val state: StateFlow<BTScannerViewState> = _state

    init {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                btController.scannedDeviceList
                    .combine(btDeviceLocalRepository.getBTDeviceList()) { scannedDeviceList, repoDeviceList ->
                        scannedDeviceList.map { scannedDevice ->
                            scannedDevice.copy(
                                isSaved = repoDeviceList.any {
                                    it.macAddress == scannedDevice.macAddress
                                }
                            )
                        }
                    }
                    .collect { scannedDeviceList ->
                        _state.update { it.copy(scannedDeviceList = scannedDeviceList) }
                    }
            }
        }
    }

    fun onAction(action: BTScannerAction) {
        when (action) {
            BTScannerAction.StartScanning -> startScanning()
            BTScannerAction.StopScanning -> stopScanning()
            is BTScannerAction.AddDeviceToRepo -> addDeviceToRepo(action.btDevice)
            is BTScannerAction.RemoveDeviceFromRepo -> removeDeviceFromRepo(action.btDevice)
        }
    }

    private fun startScanning() {
        btController.startDiscovery()
    }

    private fun stopScanning() {
        btController.stopDiscovery()
    }

    private fun addDeviceToRepo(btDevice: BTDeviceDomain) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                btDeviceLocalRepository.insertBTDevice(
                    btDevice.copy(isSaved = true)
                )
            }
        }
    }

    private fun removeDeviceFromRepo(btDevice: BTDeviceDomain) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                btDeviceLocalRepository.removeBTDevice(btDevice)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        btController.release()
    }
}
