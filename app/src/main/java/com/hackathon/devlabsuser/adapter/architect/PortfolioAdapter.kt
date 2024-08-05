package com.hackathon.devlabsuser.adapter.architect

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hackathon.devlabsuser.databinding.ItemPortfolioBinding
import com.hackathon.devlabsuser.model.Portfolio

class PortfolioAdapter(private var portfolios: List<Portfolio>) : RecyclerView.Adapter<PortfolioAdapter.PortfolioViewHolder>() {

    inner class PortfolioViewHolder(private val binding: ItemPortfolioBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(portfolio: Portfolio) {
            binding.apply {
                tvPortfolioName.text = portfolio.name
                tvPortfolioDescription.text = portfolio.description
                Glide.with(ivPortfolioTheme.context)
                    .load(portfolio.attachments?.firstOrNull()?.path)
                    .into(ivPortfolioTheme)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortfolioViewHolder {
        val binding = ItemPortfolioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PortfolioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PortfolioViewHolder, position: Int) {
        holder.bind(portfolios[position])
    }

    override fun getItemCount(): Int = portfolios.size

    fun updatePortfolios(newPortfolios: List<Portfolio>) {
        portfolios = newPortfolios
        notifyDataSetChanged()
    }
}