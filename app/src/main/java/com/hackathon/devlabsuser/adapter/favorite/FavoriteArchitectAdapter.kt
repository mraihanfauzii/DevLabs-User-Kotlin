package com.hackathon.devlabsuser.adapter.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hackathon.devlabsuser.databinding.ItemArticleHorizontalBinding
import com.hackathon.devlabsuser.model.UserData

class FavoriteArchitectAdapter: RecyclerView.Adapter<FavoriteArchitectAdapter.ArchitectListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private var architectList = emptyList<UserData>()
    inner class ArchitectListViewHolder(private val binding: ItemArticleHorizontalBinding): RecyclerView.ViewHolder(binding.root) {
        fun getArchitect(architect: UserData) {
            binding.apply {
                Glide.with(itemView)
                    .load("https://www.bumpy-insects-reply-yearly.a276.dcdg.xyz" + architect.profilePicture)
                    .centerCrop()
                    .into(imgArticleThumbnail)
                tvArticleTitle.text = architect.profileName
                tvStoriesDescription.text = architect.profileDescription

                root.setOnClickListener {
                    onItemClickCallback.onItemClicked(architect)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteArchitectAdapter.ArchitectListViewHolder {
        val data = ItemArticleHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArchitectListViewHolder(data)
    }

    override fun onBindViewHolder(holder: FavoriteArchitectAdapter.ArchitectListViewHolder, position: Int) {
        holder.getArchitect(architectList[position])
    }

    override  fun getItemCount(): Int = architectList.size

    fun getArchitects(listArchitect: List<UserData>) {
        architectList = listArchitect
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(architect: UserData)
    }
}