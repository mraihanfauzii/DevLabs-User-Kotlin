package com.hackathon.devlabsuser.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetMessageResponse(
    val success: Boolean,
    val message: String,
    val code: Int,
    val data: List<Message>
) : Parcelable

@Parcelize
data class Message(
    val id: String,
    val sender: User,
    val receiver: User,
    val message: String,
    @field:SerializedName("createdAt")
    val created_at: String
) : Parcelable

@Parcelize
data class User(
    val id: String,
    @field:SerializedName("profileNAme")
    val profile_name: String,
//    @field:SerializedName("profilePicture")
//    val profile_picture: Any?
) : Parcelable