package com.hackathon.devlabsuser.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Theme(
    val id: String,
    val name: String,
    @field:SerializedName("theme_image")
    val themeImage: String,
    @field:SerializedName("created_at")
    val createdAt: String,
    @field:SerializedName("click_count")
    val clickCount: Int
) : Parcelable