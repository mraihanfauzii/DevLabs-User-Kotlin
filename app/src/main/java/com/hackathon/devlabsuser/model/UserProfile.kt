package com.hackathon.devlabsuser.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "architect_favorite")
@Parcelize
data class UserData(
    @PrimaryKey
    val id: String,
    val email: String,
    @field:SerializedName("phonenumber")
    val phoneNumber: String,
    @field:SerializedName("profile_name")
    val profileName: String,
    @field:SerializedName("profile_picture")
    val profilePicture: String?,
    @field:SerializedName("profile_description")
    val profileDescription: String?,
    val role: String,
    @field:SerializedName("created_at")
    val createdAt: String
) : Parcelable

data class UpdateProfileData(
    val id: String
)