package uz.devapp.e_control.data.model


import com.google.gson.annotations.SerializedName
import uz.devapp.e_control.database.entity.EmployeeEntity
import uz.devapp.e_control.database.entity.PurposeEntity

data class AttendsModel(
    @SerializedName("attendance_id")
    val attendanceId: Int,
    @SerializedName("employee")
    val employee: EmployeeEntity,
    @SerializedName("moment")
    val moment: Boolean,
    @SerializedName("now_time")
    val nowTime: String,
    @SerializedName("purposes")
    val purposes: List<PurposeEntity>
)