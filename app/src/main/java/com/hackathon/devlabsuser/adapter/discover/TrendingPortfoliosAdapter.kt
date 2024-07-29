package com.hackathon.devlabsuser.adapter.discover

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hackathon.devlabsuser.R
import com.hackathon.devlabsuser.databinding.ItemPromoCardBinding
import com.hackathon.devlabsuser.model.Portfolio

class TrendingPortfoliosAdapter: RecyclerView.Adapter<TrendingPortfoliosAdapter.TrendingPortfoliosViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private var trendingPortfolioList = emptyList<Portfolio>()

    inner class TrendingPortfoliosViewHolder(private val binding: ItemPromoCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(portfolio: Portfolio) {
            binding.apply {
                Glide.with(itemView)
                    .load(portfolio.theme?.image)
                    .centerCrop()
                    .placeholder(R.drawable.contoh_profile)
                    .into(imgPromoThumbnail)
                tvPromoTitle.text = portfolio.name

                root.setOnClickListener {
                    onItemClickCallback.onItemClicked(portfolio)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrendingPortfoliosAdapter.TrendingPortfoliosViewHolder {
        val data =
            ItemPromoCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrendingPortfoliosViewHolder(data)
    }

    override fun getItemCount(): Int = trendingPortfolioList.size

    override fun onBindViewHolder(holder: TrendingPortfoliosAdapter.TrendingPortfoliosViewHolder, position: Int) {
        val trendingPortfolio = trendingPortfolioList[position]
        holder.bind(trendingPortfolio)
    }

    fun getTrendingPortfolios(newPortfolio: List<Portfolio>) {
        trendingPortfolioList = newPortfolio
        notifyDataSetChanged()
    }

    interface OnItemClickCallback {
        fun onItemClicked(portfolio: Portfolio)
    }
}