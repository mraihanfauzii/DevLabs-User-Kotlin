package com.hackathon.devlabsuser.adapter.discover

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hackathon.devlabsuser.R
import com.hackathon.devlabsuser.databinding.ItemArticleHorizontalBinding
import com.hackathon.devlabsuser.databinding.ItemPromoCardBinding
import com.hackathon.devlabsuser.model.Portfolio

class LatestPortfoliosAdapter: RecyclerView.Adapter<LatestPortfoliosAdapter.LatestPortfoliosViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private var latestPortfolioList = emptyList<Portfolio>()

    inner class LatestPortfoliosViewHolder(private val binding: ItemArticleHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(portfolio: Portfolio) {
            binding.apply {
                Glide.with(itemView)
                    .load(portfolio.theme?.image)
                    .centerCrop()
                    .placeholder(R.drawable.contoh_profile)
                    .into(imgArticleThumbnail)
                tvArticleTitle.text = portfolio.name

                root.setOnClickListener {
                    onItemClickCallback.onItemClicked(portfolio)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LatestPortfoliosAdapter.LatestPortfoliosViewHolder {
        val data =
            ItemArticleHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LatestPortfoliosViewHolder(data)
    }

    override fun getItemCount(): Int = latestPortfolioList.size

    override fun onBindViewHolder(holder: LatestPortfoliosAdapter.LatestPortfoliosViewHolder, position: Int) {
        val latestPortfolios = latestPortfolioList[position]
        holder.bind(latestPortfolios)
    }

    fun getLatestPortfolios(newPortfolio: List<Portfolio>) {
        latestPortfolioList = newPortfolio
        notifyDataSetChanged()
    }

    interface OnItemClickCallback {
        fun onItemClicked(portfolio: Portfolio)
    }
}