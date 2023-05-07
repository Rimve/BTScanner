package com.never.simplebtscanner.ui.bt_scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.never.simplebtscanner.ui.bt_scanner.utils.BTController
import com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.BTDeviceDomain
import com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.database.BTDeviceLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val btDeviceLocalRepository: BTDeviceLocalRepository
) : ViewModel() {
    private val _state = MutableStateFlow(BTScannerViewState())
    val state: StateFlow<BTScannerViewState> = _state

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                btDeviceLocalRepository.deleteTable()
                btController.scannedDeviceList
                    .combine(btDeviceLocalRepository.getBTDeviceList()) { scannedDeviceList, repoDeviceList ->
                        scannedDeviceList.map {
                            it.copy(isSaved = repoDeviceList.contains(it))
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
            withContext(Dispatchers.IO) {
                btDeviceLocalRepository.insertBTDevice(btDevice)
            }
        }
    }

    private fun removeDeviceFromRepo(btDevice: BTDeviceDomain) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                btDeviceLocalRepository.removeBTDevice(btDevice)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        btController.release()
    }
}
