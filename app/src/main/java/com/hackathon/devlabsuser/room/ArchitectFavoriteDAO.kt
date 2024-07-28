package com.hackathon.devlabsuser.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hackathon.devlabsuser.model.ArchitectFavorite

@Dao
interface ArchitectFavoriteDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(architectFavorite: ArchitectFavorite)

    @Delete
    suspend fun delete(architectFavorite: ArchitectFavorite)

    @Query("SELECT * FROM architect_favorite")
    fun getAllArchitects(): LiveData<List<ArchitectFavorite>>
}