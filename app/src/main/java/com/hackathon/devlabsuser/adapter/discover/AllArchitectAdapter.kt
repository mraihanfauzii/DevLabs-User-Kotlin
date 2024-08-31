package com.hackathon.devlabsuser.adapter.discover

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hackathon.devlabsuser.R
import com.hackathon.devlabsuser.databinding.ItemArticleHorizontalBinding
import com.hackathon.devlabsuser.model.RecArchitect
import com.hackathon.devlabsuser.model.UserData

class AllArchitectAdapter(private val onItemClickCallback: OnItemClickCallback): RecyclerView.Adapter<AllArchitectAdapter.ArchitectListViewHolder>() {
    private var architectList = emptyList<UserData>()

    inner class ArchitectListViewHolder(private val binding: ItemArticleHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(architect: UserData) {
            binding.apply {
                Glide.with(itemView)
                    .load("https://www.bumpy-insects-reply-yearly.a276.dcdg.xyz"+architect.profilePicture)
                    .centerCrop()
                    .placeholder(R.drawable.contoh_profile)
                    .into(imgArticleThumbnail)
                tvArticleTitle.text = architect.profileName

                root.setOnClickListener {
                    onItemClickCallback.onItemClicked(architect)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllArchitectAdapter.ArchitectListViewHolder {
        val data = ItemArticleHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArchitectListViewHolder(data)
    }

    override fun getItemCount(): Int = architectList.size

    override fun onBindViewHolder(holder: AllArchitectAdapter.ArchitectListViewHolder, position: Int) {
        val architect = architectList[position]
        holder.bind(architect)
    }

    fun getArchitects(newArchitect: List<UserData>) {
        architectList = newArchitect
        notifyDataSetChanged()
    }

    interface OnItemClickCallback {
        fun onItemClicked(architect: UserData)
    }
}