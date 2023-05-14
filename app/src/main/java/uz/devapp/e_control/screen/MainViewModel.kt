package uz.devapp.e_control.screen

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uz.devapp.e_control.data.model.AttendsModel
import uz.devapp.e_control.data.model.DeviceModel
import uz.devapp.e_control.data.model.request.DeviceRequest
import uz.devapp.e_control.data.model.request.PurposeRequest
import uz.devapp.e_control.data.repository.MainRepository
import uz.devapp.e_control.data.repository.sealed.DataResult
import uz.devapp.e_control.database.entity.EmployeeEntity
import uz.devapp.e_control.database.entity.PurposeEntity
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private var _progressLiveData = MutableLiveData<Boolean>()
    var progressLiveData: LiveData<Boolean> = _progressLiveData

    private var _employeeEntityLiveData = MutableLiveData<DataResult<List<EmployeeEntity>?>>()
    var employeeEntityLiveData: LiveData<DataResult<List<EmployeeEntity>?>> =
        _employeeEntityLiveData

    private var _employeeLiveData = MutableLiveData<DataResult<EmployeeEntity?>>()
    var employeeLiveData: LiveData<DataResult<EmployeeEntity?>> = _employeeLiveData

    private var _attendsLiveData = MutableLiveData<DataResult<AttendsModel>>()
    var attendsLiveData: LiveData<DataResult<AttendsModel>> = _attendsLiveData

    private var _attendsOfflineLiveData = MutableLiveData<DataResult<Any?>>()
    var attendsOfflineLiveData: LiveData<DataResult<Any?>> = _attendsOfflineLiveData

    private var _purposeLiveData = MutableLiveData<DataResult<Any?>>()
    var purposeLiveData: LiveData<DataResult<Any?>> = _purposeLiveData

    private var _purposeGetLiveData = MutableLiveData<DataResult<List<PurposeEntity>?>>()
    var purposeGetLiveData: LiveData<DataResult<List<PurposeEntity>?>> = _purposeGetLiveData

    private var _deviceLiveData = MutableLiveData<DataResult<DeviceModel>>()
    var deviceLiveData: LiveData<DataResult<DeviceModel>> = _deviceLiveData

    fun getEmployees() {
        viewModelScope.launch {
            repository.getEmployees().collect {
                _employeeEntityLiveData.value = it
            }
        }
    }

    fun getPurpose() {
        viewModelScope.launch {
            repository.getPurpose().collect {
                _purposeGetLiveData.value = it
            }
        }
    }

    fun getEmployeeByPinCode(pinCode: String) {
        viewModelScope.launch {
            repository.getEmployeeByPinCode(pinCode).collect {
                _employeeLiveData.value = it
            }
        }
    }

    fun saveAttends(image: String, type: String, employeeId: Int, deviceId: Int) {
        viewModelScope.launch {
            repository.saveAttends(image, type, employeeId, deviceId).collect {
                _attendsLiveData.value = it
            }
        }
    }

    fun saveAttendsOffline(
        image: String,
        type: String,
        date: String,
        employeeId: Int,
        deviceId: Int,
        purposeId: Int
    ) {
        viewModelScope.launch {
            repository.saveAttendsOffline(image, type, date, employeeId, deviceId, purposeId)
                .collect {
                    _attendsOfflineLiveData.value = it
                }
        }
    }

    fun setPurpose(request: PurposeRequest, context: Context) {
        viewModelScope.launch {
            repository.setPurpose(request, context).collect {
                _purposeLiveData.value = it
            }
        }
    }

    fun getDevice(request: DeviceRequest) {
        viewModelScope.launch {
            repository.getDevice(request).collect {
                _deviceLiveData.value = it
            }
        }
    }
}