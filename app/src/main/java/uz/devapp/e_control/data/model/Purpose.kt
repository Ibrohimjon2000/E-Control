package uz.devapp.e_control.data.model


import com.google.gson.annotations.SerializedName

data class Purpose(
    @SerializedName("id")
    val id: Int,
    @SerializedName("purpose")
    val purpose: String
)