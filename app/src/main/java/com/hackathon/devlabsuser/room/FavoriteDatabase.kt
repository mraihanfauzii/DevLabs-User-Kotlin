package com.hackathon.devlabsuser.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hackathon.devlabsuser.model.Article
import com.hackathon.devlabsuser.model.Portfolio
import com.hackathon.devlabsuser.model.UserData
import com.hackathon.devlabsuser.utils.Converters

@Database(
    entities = [Article::class, UserData::class, Portfolio::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class FavoriteDatabase : RoomDatabase() {
    abstract fun articleFavoriteDao(): ArticleFavoriteDAO
    abstract fun architectFavoriteDao(): ArchitectFavoriteDAO
    abstract fun portfolioFavoriteDao(): PortfolioFavoriteDAO

    companion object {
        @Volatile
        private var INSTANCE: FavoriteDatabase? = null

        fun getDatabase(context: Context): FavoriteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteDatabase::class.java,
                    "favorite_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}