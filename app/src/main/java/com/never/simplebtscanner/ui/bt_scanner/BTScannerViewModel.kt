package com.never.simplebtscanner.ui.bt_scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.never.simplebtscanner.di.IoDispatcher
import com.never.simplebtscanner.ui.bt_scanner.utils.BTController
import com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.BTDeviceDomain
import com.never.simplebtscanner.ui.bt_scanner.utils.usecases.DevicesUseCase
import com.never.simplebtscanner.ui.saved_devices.utils.usecases.SavedDevicesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BTScannerViewModel @Inject constructor(
    private val btController: BTController,
    private val devicesUseCase: DevicesUseCase,
    private val savedDevicesUseCase: SavedDevicesUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _state = MutableStateFlow(BTScannerViewState())
    val state: StateFlow<BTScannerViewState> = _state

    init {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                btController.scannedDeviceList.collect { scannedDeviceList ->
                    devicesUseCase.insertBTDeviceList(scannedDeviceList)
                }
            }
        }

        viewModelScope.launch {
            withContext(ioDispatcher) {
                devicesUseCase.getBTDeviceList().collect { repoDeviceList ->
                    _state.update { it.copy(scannedDeviceList = repoDeviceList) }
                }
            }
        }
    }

    fun onAction(action: BTScannerAction) {
        when (action) {
            BTScannerAction.StartScanning -> startScanning()
            BTScannerAction.StopScanning -> stopScanning()
            BTScannerAction.OnSearchClick -> onSearchClick()
            BTScannerAction.OnDeviceRenameDialogDismiss -> onDeviceRenameDialogDismiss()
            is BTScannerAction.SetSnackbarMessage -> setSnackbarMessage(action.message)
            is BTScannerAction.OnDeviceClick -> onDeviceClick(action.btDevice)
            is BTScannerAction.SaveDevice -> saveDeviceToRepo(action.btDevice)
            is BTScannerAction.RemoveDevice -> removeDeviceFromRepo(action.btDevice)
            is BTScannerAction.OnSearchTermUpdate -> searchDeviceByTerm(action.searchTerm)
            is BTScannerAction.OnRenameDeviceTermUpdate -> selectedDeviceNameUpdate(action.nameTerm)
            is BTScannerAction.OnRenameDevice -> renameDevice(
                action.nameTerm,
                action.btDevice
            )
        }
    }

    private fun startScanning() {
        btController.startDiscovery()
        _state.update { it.copy(isScanning = true) }
    }

    private fun stopScanning() {
        btController.stopDiscovery()
        _state.update { it.copy(isScanning = false) }
    }

    private fun onSearchClick() {
        _state.update { it.copy(isSearching = !it.isSearching) }
    }

    private fun onDeviceRenameDialogDismiss() {
        _state.update { it.copy(selectedDevice = null) }
    }

    private fun saveDeviceToRepo(btDevice: BTDeviceDomain) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                savedDevicesUseCase.insertSavedBTDevice(btDevice)
            }
        }
    }

    private fun removeDeviceFromRepo(btDevice: BTDeviceDomain) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                savedDevicesUseCase.removeSavedBTDevice(btDevice)
            }
        }
    }

    private fun searchDeviceByTerm(searchTerm: String) {
        _state.update { state ->
            state.copy(searchTerm = searchTerm)
        }
        viewModelScope.launch {
            withContext(ioDispatcher) {
                _state.update { state ->
                    state.copy(
                        searchedDeviceList = devicesUseCase.searchDevice(searchTerm)
                    )
                }
            }
        }
    }

    private fun onDeviceClick(selectedDevice: BTDeviceDomain) {
        _state.update {
            it.copy(
                selectedDevice = selectedDevice,
                selectedDeviceName = selectedDevice.name
            )
        }
    }

    private fun selectedDeviceNameUpdate(nameTerm: String) {
        _state.update {
            it.copy(selectedDeviceName = nameTerm)
        }
    }

    private fun renameDevice(newName: String?, btDevice: BTDeviceDomain) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                devicesUseCase.insertBTDevice(
                    btDevice.copy(name = newName)
                )
            }
        }
        _state.update { it.copy(selectedDevice = null) }
    }

    private fun setSnackbarMessage(message: String?) {
        _state.update { it.copy(snackbarMessage = message) }
    }

    override fun onCleared() {
        super.onCleared()
        btController.release()
    }
}
