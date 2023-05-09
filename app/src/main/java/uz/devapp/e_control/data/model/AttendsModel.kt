package uz.devapp.e_control.data.model


import com.google.gson.annotations.SerializedName

data class AttendsModel(
    @SerializedName("attendance_id")
    val attendanceId: Int,
    @SerializedName("employee")
    val employee: EmployeeModel,
    @SerializedName("moment")
    val moment: Boolean,
    @SerializedName("now_time")
    val nowTime: String,
    @SerializedName("purposes")
    val purposes: List<Purpose>
)