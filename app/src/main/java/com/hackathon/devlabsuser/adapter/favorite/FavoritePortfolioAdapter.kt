package com.hackathon.devlabsuser.adapter.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hackathon.devlabsuser.databinding.ItemArticleHorizontalBinding
import com.hackathon.devlabsuser.model.Portfolio

class FavoritePortfolioAdapter: RecyclerView.Adapter<FavoritePortfolioAdapter.PortfolioListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private var portfolioList = emptyList<Portfolio>()
    inner class PortfolioListViewHolder(private val binding: ItemArticleHorizontalBinding): RecyclerView.ViewHolder(binding.root) {
        fun getPortfolio(portfolio: Portfolio) {
            binding.apply {
                Glide.with(itemView)
                    .load(portfolio.theme?.image)
                    .centerCrop()
                    .into(imgArticleThumbnail)
                tvArticleTitle.text = portfolio.name
                tvStoriesDescription.text = portfolio.description

                root.setOnClickListener {
                    onItemClickCallback.onItemClicked(portfolio)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoritePortfolioAdapter.PortfolioListViewHolder {
        val data = ItemArticleHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PortfolioListViewHolder(data)
    }

    override fun onBindViewHolder(holder: FavoritePortfolioAdapter.PortfolioListViewHolder, position: Int) {
        holder.getPortfolio(portfolioList[position])
    }

    override  fun getItemCount(): Int = portfolioList.size

    fun getPortfolios(listPortfolio: List<Portfolio>) {
        portfolioList = listPortfolio
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(portfolio: Portfolio)
    }
}