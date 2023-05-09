package uz.devapp.e_control.data.model


import com.google.gson.annotations.SerializedName

data class EmployeeModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("position")
    val position: String
):java.io.Serializable