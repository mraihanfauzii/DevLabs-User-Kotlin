package com.hackathon.devlabsuser.ui.architect

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hackathon.devlabsuser.R
import com.hackathon.devlabsuser.databinding.ItemRecArchitectBinding
import com.hackathon.devlabsuser.model.RecArchitect

class ArchitectRecAdapter(
    private val architects: List<RecArchitect>,
    private val onItemClickCallback: OnItemClickCallbackRecArchitect
) : RecyclerView.Adapter<ArchitectRecAdapter.ArchitectViewHolder>() {

    inner class ArchitectViewHolder(private val binding: ItemRecArchitectBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(architect: RecArchitect) {
            binding.apply {
                tvName.text = architect.profileName
                tvDescription.text = architect.profileDescription
                tvRate.text = "Permeter : ${architect.rate} IDR"

                // Load profile picture using Glide
                Glide.with(itemView)
                    .load(architect.profilePicture)
                    .placeholder(R.drawable.contoh_profile) // Placeholder image
                    .into(imgProfile)

                root.setOnClickListener {
                    onItemClickCallback.onItemClicked(architect)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArchitectViewHolder {
        val data = ItemRecArchitectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArchitectViewHolder(data)
    }

    override fun getItemCount(): Int {
        return architects.size
    }

    override fun onBindViewHolder(holder: ArchitectViewHolder, position: Int) {
        val architect = architects[position]
        holder.bind(architect)
    }

    interface OnItemClickCallbackRecArchitect {
        fun onItemClicked(architect: RecArchitect)
    }
}
