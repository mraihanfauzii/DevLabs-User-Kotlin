package com.hackathon.devlabsuser.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddMessageRequest(
    @field:SerializedName("receiverId")
    val receiver_id: String,
    val message: String,
) : Parcelable

data class AddMessageResponse(
    val success: Boolean,
    val message: String,
    val code: Int,
    val data: AddMessageData
)

data class AddMessageData(
    val id: String
)