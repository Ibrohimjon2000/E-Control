package uz.devapp.e_control.data.model


import com.google.gson.annotations.SerializedName

data class DeviceModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("token")
    val token: String
)