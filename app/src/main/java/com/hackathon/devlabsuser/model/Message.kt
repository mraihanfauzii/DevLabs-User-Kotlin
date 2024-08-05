package com.hackathon.devlabsuser.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddMessageRequest(
    @field:SerializedName("receiver_id")
    val receiverId: String,
    val message: String,
) : Parcelable

data class AddMessageData(
    val id: String
)

@Parcelize
data class Message(
    val id: String,
    val sender: User,
    val receiver: User,
    val message: String,
    @field:SerializedName("created_at")
    val createdAt: String
) : Parcelable

@Parcelize
data class User(
    val id: String,
    @field:SerializedName("profile_name")
    val profileName: String,
    @field:SerializedName("profile_picture")
    val profilePicture: String?
) : Parcelable

@Parcelize
data class LastMessage(
    val id: String,
    val contact: Contact,
    val message: String,
    @field:SerializedName("created_at")
    val createdAt: String
) : Parcelable

@Parcelize
data class Contact(
    val id: String,
    @field:SerializedName("profile_name")
    val profileName: String,
    @field:SerializedName("profile_picture")
    val profilePicture: String?
) : Parcelable