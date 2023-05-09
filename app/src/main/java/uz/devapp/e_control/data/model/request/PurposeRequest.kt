package uz.devapp.e_control.data.model.request


import com.google.gson.annotations.SerializedName

data class PurposeRequest(
    @SerializedName("attendance_id")
    val attendanceId: Int,
    @SerializedName("device_id")
    val deviceId: Int,
    @SerializedName("employee_id")
    val employeeId: Int,
    @SerializedName("purpose_id")
    val purposeId: Int
)