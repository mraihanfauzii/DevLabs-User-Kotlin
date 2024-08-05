package com.hackathon.devlabsuser.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Rating(
    val id: String,
    val rater: User,
    val ratee: User,
    val rating: Int,
    val description: String,
    @field:SerializedName("created_at")
    val createdAt: String,
    val attachments: List<Attachment>?
) : Parcelable

@Parcelize
data class AverageRating(
    @field:SerializedName("average_rating")
    val averageRating: String
) : Parcelable

@Parcelize
data class Attachment(
    val id: String,
    @field:SerializedName("rating_id")
    val ratingId: String,
    val name: String,
    val path: String,
    @field:SerializedName("created_at")
    val createdAt: String
) : Parcelable