package com.hackathon.devlabsuser.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddPortfolioRequest(
    val name: String,
    val description: String,
    @field:SerializedName("attachment_files")
    val attachmentFiles: List<String>
) : Parcelable

data class AddPortfolioResponse(
    val message: String,
    val code: Int,
    val data: AddPortfolioData
)

data class AddPortfolioData(
    val id: String
)

data class GetPortfolioResponse(
    val success: Boolean,
    val message: String,
    val code: Int,
    val data: List<Portfolio>
)

@Parcelize
data class Portfolio(
    val id: String,
    @field:SerializedName("architect_id")
    val architectId: String,
    val name: String,
    val description: String,
    @field:SerializedName("created_at")
    val createdAt: String,
//    val attachments: Any?
) : Parcelable