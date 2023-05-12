package uz.devapp.e_control.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class AttendsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val image: String,
    val type:String,
    @ColumnInfo(name = "employee_id")
    val employeeId:Int,
    @ColumnInfo(name = "device_id")
    val deviceId:Int,
    val date:Long,
    @ColumnInfo(name = "purpose_id")
    val purposeId:Int=0
)