package com.hackathon.devlabsuser.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddPortfolioRequest(
    val name: String,
    val description: String,
    @field:SerializedName("attachmentFiles")
    val attachment_files: List<String>
) : Parcelable

data class AddPortfolioResponse(
    val message: String,
    val code: Int,
    val data: AddPortfolioData
)

data class AddPortfolioData(
    val id: String
)