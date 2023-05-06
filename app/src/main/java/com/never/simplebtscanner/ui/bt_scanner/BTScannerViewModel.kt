package com.never.simplebtscanner.ui.bt_scanner

import androidx.lifecycle.ViewModel
import com.never.simplebtscanner.ui.bt_scanner.utils.BTController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class BTScannerViewModel @Inject constructor(private val btController: BTController) : ViewModel() {
    private val _state = MutableStateFlow(BTScannerViewState())

    fun startScanning() {
        btController.startDiscovery()
    }

    fun stopScanning() {
        btController.stopDiscovery()
    }

    override fun onCleared() {
        super.onCleared()
        btController.release()
    }
}
