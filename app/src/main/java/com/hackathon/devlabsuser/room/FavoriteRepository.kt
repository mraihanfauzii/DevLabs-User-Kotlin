package com.hackathon.devlabsuser.room

import androidx.lifecycle.LiveData
import com.hackathon.devlabsuser.model.ArchitectFavorite
import com.hackathon.devlabsuser.model.Article
import com.hackathon.devlabsuser.model.PortfolioFavorite

class FavoriteRepository(private val database: FavoriteDatabase) {
    val allArticleFavorites: LiveData<List<Article>> = database.articleFavoriteDao().getAllArticles()
    val allArchitectFavorites: LiveData<List<ArchitectFavorite>> = database.architectFavoriteDao().getAllArchitects()
    val allPortfolioFavorites: LiveData<List<PortfolioFavorite>> = database.portfolioFavoriteDao().getAllPortfolios()

    suspend fun insertArticleFavorite(article: Article) {
        database.articleFavoriteDao().insert(article)
    }

    suspend fun deleteArticleFavorite(article: Article) {
        database.articleFavoriteDao().delete(article)
    }

    suspend fun insertArchitectFavorite(architectFavorite: ArchitectFavorite) {
        database.architectFavoriteDao().insert(architectFavorite)
    }

    suspend fun deleteArchitectFavorite(architectFavorite: ArchitectFavorite) {
        database.architectFavoriteDao().delete(architectFavorite)
    }

    suspend fun insertPortfolioFavorite(portfolioFavorite: PortfolioFavorite) {
        database.portfolioFavoriteDao().insert(portfolioFavorite)
    }

    suspend fun deletePortfolioFavorite(portfolioFavorite: PortfolioFavorite) {
        database.portfolioFavoriteDao().delete(portfolioFavorite)
    }
}