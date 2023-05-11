package uz.devapp.e_control.data.repository

import android.content.Context
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import uz.devapp.e_control.data.api.Api
import uz.devapp.e_control.data.model.request.PurposeRequest
import uz.devapp.e_control.data.repository.sealed.DataResult
import uz.devapp.e_control.utils.NetworkHelper

class MainRepository(private val api: Api) {
    private var networkHelper: NetworkHelper? = null

    suspend fun getEmployeeByPinCode(pinCode: String, context: Context) = flow {
        networkHelper = NetworkHelper(context)
        if (networkHelper?.isNetworkConnected() == true) {
            emit(DataResult.LoadingShow())
            val result = api.getEmployeeByPinCode(pinCode)
            if (result.isSuccessful) {
                if (result.body()?.success == true) {
                    emit(DataResult.LoadingHide())
                    emit(DataResult.Success(result.body()?.data))
                } else {
                    emit(DataResult.LoadingHide())
                    emit(DataResult.Error(result.body()?.message ?: ""))
                }
            } else {
                emit(DataResult.LoadingHide())
                emit(DataResult.Error(result.message()))
            }
        } else {
            emit(DataResult.Error("Internet not connection"))
        }
    }

    suspend fun setPurpose(request: PurposeRequest, context: Context) = flow {
        networkHelper = NetworkHelper(context)
        if (networkHelper?.isNetworkConnected() == true) {
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
        } else {
            emit(DataResult.Error("Internet not connection"))
        }
    }

    suspend fun saveAttends(
        image: String,
        type: String,
        employeeId: Int,
        deviceId: Int,
        context: Context
    ) = flow {
        networkHelper = NetworkHelper(context)
        if (networkHelper?.isNetworkConnected() == true) {
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
        } else {
            emit(DataResult.Error("Internet not connection"))
        }
    }
}