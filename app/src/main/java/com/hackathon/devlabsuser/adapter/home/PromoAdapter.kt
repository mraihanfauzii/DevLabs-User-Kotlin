package com.hackathon.devlabsuser.adapter.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hackathon.devlabsuser.adapter.home.article.ArticleHomeAdapter
import com.hackathon.devlabsuser.databinding.ItemPromoCardBinding
import com.hackathon.devlabsuser.model.Promo

class PromoAdapter: RecyclerView.Adapter<PromoAdapter.PromoListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private var promoList = emptyList<Promo>()

    inner class PromoListViewHolder(private val binding: ItemPromoCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(promo: Promo) {
            binding.apply {
                Glide.with(itemView)
                    .load(promo.img)
                    .centerCrop()
                    .into(imgPromoThumbnail)
                tvPromoTitle.text = promo.name

                root.setOnClickListener {
                    onItemClickCallback.onItemClicked(promo)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PromoListViewHolder {
        val data =
            ItemPromoCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PromoListViewHolder(data)
    }

    override fun getItemCount(): Int = promoList.size

    override fun onBindViewHolder(holder: PromoListViewHolder, position: Int) {
        val promo = promoList[position]
        holder.bind(promo)
    }

    fun getPromos(newPromo: List<Promo>) {
        promoList = newPromo
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: PromoAdapter.OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(promo: Promo)
    }
}