package com.hackathon.devlabsuser.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "article_favorite")
data class Article(
    @PrimaryKey
    val id: Long,
    val photoUrl: String,
    val title: String,
    val description: String
)