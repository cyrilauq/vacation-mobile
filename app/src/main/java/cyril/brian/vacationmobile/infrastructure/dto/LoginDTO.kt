package cyril.brian.vacationmobile.infrastructure.dto

import com.google.gson.annotations.SerializedName

data class LoginDTO(
    @SerializedName("userName") val userName: String,
    @SerializedName("password") val password: String
)