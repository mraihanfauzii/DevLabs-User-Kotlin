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

data class AddPortfolioData(
    val id: String
)

@Parcelize
data class Portfolio(
    val id: String,
    val architect: Architect,
    val theme: PortfolioTheme?,
    val name: String,
    val description: String,
    @field:SerializedName("estimated_budget")
    val estimatedBudget: Int?,
    @field:SerializedName("created_at")
    val createdAt: String,
    @field:SerializedName("click_count")
    val clickCount: String
) : Parcelable

@Parcelize
data class PortfolioTheme(
    val id: String,
    val name: String,
    val image: String
) : Parcelable

@Parcelize
data class Architect(
    val id: String,
    val name: String,
    val picture: String
) : Parcelable