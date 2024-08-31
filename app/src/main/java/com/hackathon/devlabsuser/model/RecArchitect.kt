package com.hackathon.devlabsuser.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecArchitect(
    val id: String,
    val email: String,
    @field:SerializedName("phonenumber")
    val phoneNumber: String,
    @field:SerializedName("profile_name")
    val profileName: String,
    @field:SerializedName("profile_picture")
    val profilePicture: String?, // Bisa null sesuai respons
    @field:SerializedName("profile_description")
    val profileDescription: String,
    val role: String,
    val city: String,
    val rate: Int,
    @field:SerializedName("most_frequent_theme")
    val mostFrequentTheme: String,
    val distance: Double
) : Parcelable

data class RecommendedArchitectsRequest(
    val area: Int,
    val city: String,
    val theme: String,
    val budget: String
)