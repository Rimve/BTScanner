package com.never.simplebtscanner.ui.bt_scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.never.simplebtscanner.di.IoDispatcher
import com.never.simplebtscanner.ui.bt_scanner.utils.BTController
import com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.BTDeviceDomain
import com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.database.BTDeviceLocalRepository
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
    private val btDeviceLocalRepository: BTDeviceLocalRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _state = MutableStateFlow(BTScannerViewState())
    val state: StateFlow<BTScannerViewState> = _state

    init {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                btController.scannedDeviceList.collect { scannedDeviceList ->
                    btDeviceLocalRepository.insertBTDeviceList(scannedDeviceList)
                }
            }
        }

        viewModelScope.launch {
            withContext(ioDispatcher) {
                btDeviceLocalRepository.getBTDeviceList().collect { repoDeviceList ->
                    _state.update { it.copy(scannedDeviceList = repoDeviceList) }
                }
            }
        }
    }

    fun onAction(action: BTScannerAction) {
        when (action) {
            BTScannerAction.StartScanning -> startScanning()
            BTScannerAction.StopScanning -> stopScanning()
            BTScannerAction.OnSearchClick -> _state.update { it.copy(isSearching = !it.isSearching) }
            BTScannerAction.OnDeviceRenameDialogDismiss -> _state.update { it.copy(selectedDevice = null) }
            is BTScannerAction.OnDeviceClick -> onDeviceClick(action.btDevice)
            is BTScannerAction.SaveDevice -> saveDeviceToRepo(action.btDevice)
            is BTScannerAction.RemoveDevice -> removeDeviceFromRepo(action.btDevice)
            is BTScannerAction.OnSearchTermUpdate -> searchDeviceByTerm(action.searchTerm)
            is BTScannerAction.OnRenameDeviceTermUpdate -> selectedDeviceNameUpdate(action.nameTerm)
            is BTScannerAction.OnRenameDevice -> renameDevice(action.nameTerm, action.btDevice)
            is BTScannerAction.SetSnackbarMessage -> setSnackbarMessage(action.message)
        }
    }

    private fun startScanning() {
        btController.startDiscovery()
    }

    private fun stopScanning() {
        btController.stopDiscovery()
    }

    private fun saveDeviceToRepo(btDevice: BTDeviceDomain) {
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
                btDeviceLocalRepository.insertBTDevice(
                    btDevice.copy(isSaved = false)
                )
            }
        }
    }

    private fun searchDeviceByTerm(searchTerm: String) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                _state.update { state ->
                    val foundDeviceList = state.scannedDeviceList.filter {
                        it.macAddress.contains(searchTerm, ignoreCase = true) ||
                                (it.name?.contains(searchTerm, ignoreCase = true) ?: false)
                    }
                    state.copy(
                        searchTerm = searchTerm,
                        searchedDeviceList = foundDeviceList
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
                btDeviceLocalRepository.insertBTDevice(
                    btDevice.copy(name = newName)
                )
                _state.update { it.copy(selectedDevice = null) }
            }
        }
    }

    private fun setSnackbarMessage(message: String?) {
        _state.update { it.copy(snackbarMessage = message) }
    }

    override fun onCleared() {
        super.onCleared()
        btController.release()
    }
}
