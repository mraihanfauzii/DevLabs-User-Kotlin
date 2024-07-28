package com.hackathon.devlabsuser.adapter.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hackathon.devlabsuser.databinding.ItemArticleHorizontalBinding
import com.hackathon.devlabsuser.model.Article

class FavoriteArchitectAdapter: RecyclerView.Adapter<FavoriteArchitectAdapter.ArticleListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private var articleList = emptyList<Article>()
    inner class ArticleListViewHolder(private val binding: ItemArticleHorizontalBinding): RecyclerView.ViewHolder(binding.root) {
        fun getArticle(article: Article) {
            binding.apply {
                Glide.with(itemView)
                    .load(article.photoUrl)
                    .centerCrop()
                    .into(imgArticleThumbnail)
                tvArticleTitle.text = article.title
                tvStoriesDescription.text = article.description

                root.setOnClickListener {
                    onItemClickCallback.onItemClicked(article)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteArchitectAdapter.ArticleListViewHolder {
        val data = ItemArticleHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleListViewHolder(data)
    }

    override fun onBindViewHolder(holder: FavoriteArchitectAdapter.ArticleListViewHolder, position: Int) {
        holder.getArticle(articleList[position])
    }

    override  fun getItemCount(): Int = articleList.size

    fun getArticles(listArticle: List<Article>) {
        articleList = listArticle
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(article: Article)
    }
}