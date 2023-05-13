package uz.devapp.e_control.data.model.request


import com.google.gson.annotations.SerializedName

data class DeviceRequest(
    @SerializedName("id")
    val id: Int,
    @SerializedName("password")
    val password: String
)