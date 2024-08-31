package com.hackathon.devlabsuser.ui.architect

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hackathon.devlabsuser.R
import com.hackathon.devlabsuser.model.RecArchitect

class ArchitectAdapter(private val architects: List<RecArchitect>) :
    RecyclerView.Adapter<ArchitectAdapter.ArchitectViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArchitectViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rec_architect, parent, false)
        return ArchitectViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArchitectViewHolder, position: Int) {
        val architect = architects[position]
        holder.bind(architect)
    }

    override fun getItemCount(): Int {
        return architects.size
    }

    class ArchitectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgProfile: ImageView = itemView.findViewById(R.id.img_profile)
        private val tvName: TextView = itemView.findViewById(R.id.tv_name)
        private val tvDescription: TextView = itemView.findViewById(R.id.tv_description)
        private val tvRate: TextView = itemView.findViewById(R.id.tv_rate)

        fun bind(architect: RecArchitect) {
            tvName.text = architect.profileName
            tvDescription.text = architect.profileDescription
            tvRate.text = "${architect.rate} IDR"

            // Load gambar profil menggunakan Glide
            Glide.with(itemView.context)
                .load(architect.profilePicture)
                .placeholder(R.drawable.article_background) // Gambar placeholder
                .into(imgProfile)
        }
    }
}