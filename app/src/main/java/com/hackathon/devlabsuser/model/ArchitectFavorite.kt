package com.hackathon.devlabsuser.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "architect_favorite")
data class ArchitectFavorite(
    @PrimaryKey
    val id: Int,
    val name: String,
    val photoUrl: String,
    val bio: String,
    val theme: String,
    val rating: String
) : Serializable