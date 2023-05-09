package uz.devapp.e_control.data.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import uz.devapp.e_control.data.model.AttendsModel
import uz.devapp.e_control.data.model.EmployeeModel
import uz.devapp.e_control.data.model.request.PurposeRequest
import uz.devapp.e_control.data.model.response.BaseResponse

interface Api {

    @GET("api/employee/{pin_code}/find")
    suspend fun getEmployeeByPinCode(
        @Path("pin_code") pinCode: String
    ): Response<BaseResponse<EmployeeModel>>

    @Multipart
    @POST("api/attendance/add")
    suspend fun saveAttends(
        @Part image: MultipartBody.Part,
        @Part("type") type: RequestBody,
        @Part("employee_id") employeeId: RequestBody,
        @Part("device_id") deviceId: RequestBody,
    ): Response<BaseResponse<AttendsModel>>

    @POST("api/attendance/late/purpose")
    suspend fun setPurpose(
        @Body request: PurposeRequest
    ): Response<BaseResponse<Any?>>
}