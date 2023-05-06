package com.never.simplebtscanner.ui.bt_scanner

import androidx.lifecycle.ViewModel
import com.never.simplebtscanner.ui.bt_scanner.utils.BTController
import com.never.simplebtscanner.ui.bt_scanner.utils.domain.BTDeviceDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BTScannerViewModel @Inject constructor(private val btController: BTController) : ViewModel() {
    private val _state = MutableStateFlow(BTScannerViewState())
    val state: StateFlow<BTScannerViewState> = _state

    fun onAction(action: BTScannerAction) {
        when (action) {
            BTScannerAction.StartScanning -> startScanning()
            BTScannerAction.StopScanning -> stopScanning()
            is BTScannerAction.OnScannedResult -> updateScannedDeviceList(action.scannedDeviceList)
        }
    }

    private fun startScanning() {
        btController.startDiscovery()
    }

    private fun stopScanning() {
        btController.stopDiscovery()
    }

    private fun updateScannedDeviceList(scannedDeviceList: List<BTDeviceDomain>) {
        _state.update {
            it.copy(scannedDeviceList = scannedDeviceList)
        }
    }

    override fun onCleared() {
        super.onCleared()
        btController.release()
    }
}
