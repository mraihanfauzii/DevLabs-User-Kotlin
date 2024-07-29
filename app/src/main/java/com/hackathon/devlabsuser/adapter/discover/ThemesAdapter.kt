package com.hackathon.devlabsuser.adapter.discover

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hackathon.devlabsuser.R
import com.hackathon.devlabsuser.databinding.ItemPromoCardBinding
import com.hackathon.devlabsuser.model.Theme

class ThemesAdapter: RecyclerView.Adapter<ThemesAdapter.ThemesViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private var themeList = emptyList<Theme>()

    inner class ThemesViewHolder(private val binding: ItemPromoCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(theme: Theme) {
            binding.apply {
                Glide.with(itemView)
                    .load(theme.themeImage)
                    .centerCrop()
                    .placeholder(R.drawable.contoh_profile)
                    .into(imgPromoThumbnail)
                tvPromoTitle.text = theme.name

                root.setOnClickListener {
                    onItemClickCallback.onItemClicked(theme)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ThemesAdapter.ThemesViewHolder {
        val data =
            ItemPromoCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ThemesViewHolder(data)
    }

    override fun getItemCount(): Int = themeList.size

    override fun onBindViewHolder(holder: ThemesAdapter.ThemesViewHolder, position: Int) {
        val theme = themeList[position]
        holder.bind(theme)
    }

    fun getThemes(newTheme: List<Theme>) {
        themeList = newTheme
        notifyDataSetChanged()
    }

    interface OnItemClickCallback {
        fun onItemClicked(theme: Theme)
    }
}