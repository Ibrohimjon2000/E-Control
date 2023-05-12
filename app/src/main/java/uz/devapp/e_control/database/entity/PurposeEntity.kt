package uz.devapp.e_control.database.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class PurposeEntity(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,
    @SerializedName("purpose")
    val purpose: String
)