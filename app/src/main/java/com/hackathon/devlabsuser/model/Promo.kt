package com.hackathon.devlabsuser.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class GetPromoResponse(
    val success: Boolean,
    val message: String,
    val code: Int,
    val data: List<Promo>
)

@Parcelize
data class Promo(
    val id: String,
    val name: String,
    val img: String,
    @field:SerializedName("created_at")
    val createdAt: String
) : Parcelable