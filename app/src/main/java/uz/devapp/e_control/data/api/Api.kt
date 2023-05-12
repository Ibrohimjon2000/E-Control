package uz.devapp.e_control.data.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import uz.devapp.e_control.data.model.AttendsModel
import uz.devapp.e_control.database.entity.EmployeeEntity
import uz.devapp.e_control.data.model.request.PurposeRequest
import uz.devapp.e_control.data.model.response.BaseResponse
import uz.devapp.e_control.database.entity.PurposeEntity

interface Api {

    @GET("api/employee/{pin_code}/find")
    suspend fun getEmployeeByPinCode(
        @Path("pin_code") pinCode: String
    ): Response<BaseResponse<EmployeeEntity>>

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

    @GET("api/employee/list")
    suspend fun getEmployees():Response<BaseResponse<List<EmployeeEntity>>>

    @GET("api/purpose/list")
    suspend fun getPurpose():Response<BaseResponse<List<PurposeEntity>>>

    @Multipart
    @POST("api/attendance/add/offline")
    suspend fun saveAttendsOffline(
        @Part image: MultipartBody.Part,
        @Part("type") type: RequestBody,
        @Part("date") date: RequestBody,
        @Part("employee_id") employeeId: RequestBody,
        @Part("device_id") deviceId: RequestBody,
        @Part("purpose_id") purposeId: RequestBody,
    ): Response<BaseResponse<Any?>>
}