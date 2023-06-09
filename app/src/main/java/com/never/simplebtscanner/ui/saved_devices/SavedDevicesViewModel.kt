package com.never.simplebtscanner.ui.saved_devices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.never.simplebtscanner.di.IoDispatcher
import com.never.simplebtscanner.ui.bt_scanner.utils.bt_device.BTDeviceDomain
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
class SavedDevicesViewModel @Inject constructor(
    private val savedDevicesUseCase: SavedDevicesUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _state = MutableStateFlow(SavedDevicesViewState())
    val state: StateFlow<SavedDevicesViewState> = _state

    init {
        viewModelScope.launch {
            savedDevicesUseCase.getSavedDeviceList().collect { savedDeviceList ->
                _state.update { it.copy(savedDevices = savedDeviceList) }
            }
        }
    }

    fun onAction(action: SavedDevicesAction) {
        when (action) {
            is SavedDevicesAction.RemoveDeviceFromRepo -> removeDeviceFromRepo(action.btDevice)
        }
    }

    private fun removeDeviceFromRepo(btDevice: BTDeviceDomain) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                savedDevicesUseCase.removeSavedBTDevice(btDevice)
            }
        }
    }
}
