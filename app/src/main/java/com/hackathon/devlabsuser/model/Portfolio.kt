package com.hackathon.devlabsuser.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "portfolio_favorite")
@Parcelize
data class Portfolio(
    @PrimaryKey
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
    val clickCount: String,
    val attachments: List<Attachments>?
) : Parcelable

@Parcelize
data class Attachments(
    val id: String,
    @field:SerializedName("portfolio_id")
    val portfolioId: String?,
    val name: String,
    val path: String,
    @field:SerializedName("created_at")
    val createdAt: String
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