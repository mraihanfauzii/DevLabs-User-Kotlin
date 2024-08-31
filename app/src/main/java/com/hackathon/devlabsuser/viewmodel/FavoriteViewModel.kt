package com.hackathon.devlabsuser.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.hackathon.devlabsuser.model.Article
import com.hackathon.devlabsuser.model.Portfolio
import com.hackathon.devlabsuser.room.FavoriteDatabase
import com.hackathon.devlabsuser.room.FavoriteRepository
import com.hackathon.devlabsuser.model.UserData
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FavoriteRepository
    val allArticleFavorites: LiveData<List<Article>>
    val allArchitectFavorites: LiveData<List<UserData>>
    val allPortfolioFavorites: LiveData<List<Portfolio>>

    init {
        val database = FavoriteDatabase.getDatabase(application)
        repository = FavoriteRepository(database)
        allArticleFavorites = repository.allArticleFavorites
        allArchitectFavorites = repository.allArchitectFavorites
        allPortfolioFavorites = repository.allPortfolioFavorites
    }

    fun insertArticleFavorite(article: Article) = viewModelScope.launch {
        repository.insertArticleFavorite(article)
    }

    fun deleteArticleFavorite(article: Article) = viewModelScope.launch {
        repository.deleteArticleFavorite(article)
    }

    fun insertArchitectFavorite(userData: UserData) = viewModelScope.launch {
        repository.insertArchitectFavorite(userData)
    }

    fun deleteArchitectFavorite(userData: UserData) = viewModelScope.launch {
        repository.deleteArchitectFavorite(userData)
    }

    fun insertPortfolioFavorite(portfolioFavorite: Portfolio) = viewModelScope.launch {
        repository.insertPortfolioFavorite(portfolioFavorite)
    }

    fun deletePortfolioFavorite(portfolioFavorite: Portfolio) = viewModelScope.launch {
        repository.deletePortfolioFavorite(portfolioFavorite)
    }
}