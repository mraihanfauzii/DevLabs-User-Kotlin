package com.hackathon.devlabsuser.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hackathon.devlabsuser.model.Portfolio

@Dao
interface PortfolioFavoriteDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(portfolioFavorite: Portfolio)

    @Delete
    suspend fun delete(portfolioFavorite: Portfolio)

    @Query("SELECT * FROM portfolio_favorite")
    fun getAllPortfolios(): LiveData<List<Portfolio>>
}