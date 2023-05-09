package uz.devapp.e_control.data.repository

import kotlinx.coroutines.flow.flow
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import uz.devapp.e_control.data.api.Api
import uz.devapp.e_control.data.model.request.PurposeRequest
import uz.devapp.e_control.data.repository.sealed.DataResult

class MainRepository(private val api: Api) : BaseRepository() {

    suspend fun getEmployeeByPinCode(pinCode: String) = flow {
        emit(DataResult.LoadingShow())
        val result = api.getEmployeeByPinCode(pinCode)
        if (result.isSuccessful) {
            if (result.body()?.success == true) {
                emit(DataResult.LoadingHide())
                emit(DataResult.Success(result.body()?.data))
            } else {
                emit(DataResult.Error(result.body()?.message ?: ""))
            }
        } else {
            emit(DataResult.Error(result.message()))
        }
    }

    suspend fun setPurpose(request:PurposeRequest) = flow {
        emit(DataResult.LoadingShow())
        val result = api.setPurpose(request)
        if (result.isSuccessful) {
            if (result.body()?.success == true) {
                emit(DataResult.LoadingHide())
                emit(DataResult.Success(result.body()?.data))
            } else {
                emit(DataResult.Error(result.body()?.message ?: ""))
            }
        } else {
            emit(DataResult.Error(result.message()))
        }
    }

    suspend fun saveAttends(image: String, type: String, employeeId: Int, deviceId: Int) = flow {
        emit(DataResult.LoadingShow())
        val file = java.io.File(image)
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)

        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

        val typeBody: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            type
        )

        val employeeIdBody: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            employeeId.toString()
        )

        val deviceIdBody: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            deviceId.toString()
        )

        val result = api.saveAttends(body, typeBody, employeeIdBody, deviceIdBody)
        if (result.isSuccessful) {
            if (result.body()?.success == true) {
                emit(DataResult.LoadingHide())
                emit(DataResult.Success(result.body()?.data!!))
            } else {
                emit(DataResult.Error(result.body()?.message ?: ""))
            }
        } else {
            emit(DataResult.Error(result.message()))
        }
    }
}