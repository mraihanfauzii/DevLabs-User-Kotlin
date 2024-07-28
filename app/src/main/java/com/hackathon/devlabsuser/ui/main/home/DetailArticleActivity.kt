package com.hackathon.devlabsuser.ui.main.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.hackathon.devlabsuser.R
import com.hackathon.devlabsuser.databinding.ActivityDetailArticleBinding
import com.hackathon.devlabsuser.model.Article
import com.hackathon.devlabsuser.viewmodel.FavoriteViewModel

class DetailArticleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailArticleBinding
    private val favoriteViewModel: FavoriteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra(TITLE)
        val photoUrl = intent.getStringExtra(PHOTO_URL)
        val description = intent.getStringExtra(DESCRIPTION)
        val id = intent.getStringExtra(ID).toString().toLong()

        binding.apply {
            Glide.with(this@DetailArticleActivity)
                .load(photoUrl)
                .centerCrop()
                .into(ivPhotoUrl)
            tvArticleTitle.text = title
            tvStoryDescription.text = description

            favoriteViewModel.allArticleFavorites.observe(this@DetailArticleActivity) { favorites ->
                val isFavorited = favorites.any { it.id == id }
                toggleFavorite.isChecked = isFavorited
                toggleFavorite.setBackgroundResource(if (isFavorited) R.drawable.ic_favorite_after else R.drawable.ic_favorite_before)
            }

            toggleFavorite.setOnCheckedChangeListener { _, isChecked ->
                val article = Article(id = id ?: 1,title = title ?: "", photoUrl = photoUrl ?: "", description = description ?: "")
                if (isChecked) {
                    toggleFavorite.setBackgroundResource(R.drawable.ic_favorite_after)
                    favoriteViewModel.insertArticleFavorite(article)
                } else {
                    toggleFavorite.setBackgroundResource(R.drawable.ic_favorite_before)
                    favoriteViewModel.deleteArticleFavorite(article)
                }
            }
        }
    }

    companion object {
        const val TITLE = "title"
        const val DESCRIPTION = "description"
        const val PHOTO_URL = "photo_url"
        const val ID = "photo_id"
    }
}