package com.hackathon.devlabsuser.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class GetPortfolioResponse(
    val success: Boolean,
    val message: String,
    val code: Int,
    val data: List<Portfolio>
)

@Parcelize
data class Portfolio(
    val id: String,
    @field:SerializedName("architectId")
    val architect_id: String,
    val name: String,
    val description: String,
    @field:SerializedName("createdAt")
    val created_at: String,
//    val attachments: Any?
) : Parcelable