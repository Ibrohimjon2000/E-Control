package uz.devapp.e_control.screen

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uz.devapp.e_control.data.model.AttendsModel
import uz.devapp.e_control.data.model.EmployeeModel
import uz.devapp.e_control.data.model.request.PurposeRequest
import uz.devapp.e_control.data.repository.MainRepository
import uz.devapp.e_control.data.repository.sealed.DataResult
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private var _progressLiveData = MutableLiveData<Boolean>()
    var progressLiveData: LiveData<Boolean> = _progressLiveData

    private var _employeeLiveData = MutableLiveData<DataResult<EmployeeModel?>>()
    var employeeLiveData: LiveData<DataResult<EmployeeModel?>> = _employeeLiveData

    private var _attendsLiveData = MutableLiveData<DataResult<AttendsModel>>()
    var attendsLiveData: LiveData<DataResult<AttendsModel>> = _attendsLiveData

    private var _purposeLiveData = MutableLiveData<DataResult<Any?>>()
    var purposeLiveData: LiveData<DataResult<Any?>> = _purposeLiveData

    fun getEmployeeByPinCode(pinCode: String,context:Context) {
        viewModelScope.launch {
            repository.getEmployeeByPinCode(pinCode, context).collect {
                _employeeLiveData.value = it
            }
        }
    }

    fun saveAttends(image: String, type: String, employeeId: Int, deviceId: Int,context: Context) {
        viewModelScope.launch {
            repository.saveAttends(image, type, employeeId, deviceId,context).collect {
                _attendsLiveData.value = it
            }
        }
    }

    fun setPurpose(request: PurposeRequest,context: Context) {
        viewModelScope.launch {
            repository.setPurpose(request,context).collect {
                _purposeLiveData.value = it
            }
        }
    }
}