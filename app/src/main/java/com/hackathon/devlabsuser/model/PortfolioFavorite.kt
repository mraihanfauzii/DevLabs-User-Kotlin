package com.hackathon.devlabsuser.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "portfolio_favorite")
data class PortfolioFavorite(
    @PrimaryKey
    val id: Int,
    val name: String,
    val theme: String,
    val photoUrl: String
) : Serializable