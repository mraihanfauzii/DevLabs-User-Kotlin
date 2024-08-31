package com.hackathon.devlabsuser.room

import androidx.lifecycle.LiveData
import com.hackathon.devlabsuser.model.Article
import com.hackathon.devlabsuser.model.Portfolio
import com.hackathon.devlabsuser.model.UserData

class FavoriteRepository(private val database: FavoriteDatabase) {
    val allArticleFavorites: LiveData<List<Article>> = database.articleFavoriteDao().getAllArticles()
    val allArchitectFavorites: LiveData<List<UserData>> = database.architectFavoriteDao().getAllArchitects()
    val allPortfolioFavorites: LiveData<List<Portfolio>> = database.portfolioFavoriteDao().getAllPortfolios()

    suspend fun insertArticleFavorite(article: Article) {
        database.articleFavoriteDao().insert(article)
    }

    suspend fun deleteArticleFavorite(article: Article) {
        database.articleFavoriteDao().delete(article)
    }

    suspend fun insertArchitectFavorite(architectFavorite: UserData) {
        database.architectFavoriteDao().insert(architectFavorite)
    }

    suspend fun deleteArchitectFavorite(architectFavorite: UserData) {
        database.architectFavoriteDao().delete(architectFavorite)
    }

    suspend fun insertPortfolioFavorite(portfolioFavorite: Portfolio) {
        database.portfolioFavoriteDao().insert(portfolioFavorite)
    }

    suspend fun deletePortfolioFavorite(portfolioFavorite: Portfolio) {
        database.portfolioFavoriteDao().delete(portfolioFavorite)
    }
}