package com.hackathon.devlabsuser.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegisterRequest(
    @field:SerializedName("profile_name")
    val profileName: String,
    val email: String,
    val password: String,
    @field:SerializedName("phonenumber")
    val phoneNumber: String,
    val role: String
) : Parcelable

data class RegisterResponse(
    val success: Boolean,
    val message: String,
    val code: Int,
    val data: RegisterData
)

data class RegisterData(
    val id: String
)