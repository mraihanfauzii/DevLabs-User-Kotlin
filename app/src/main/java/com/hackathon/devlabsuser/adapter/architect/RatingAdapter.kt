package com.hackathon.devlabsuser.adapter.architect

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hackathon.devlabsuser.databinding.ItemRatingBinding
import com.hackathon.devlabsuser.model.Rating

class RatingAdapter(private var ratings: List<Rating>) : RecyclerView.Adapter<RatingAdapter.RatingViewHolder>() {

    inner class RatingViewHolder(private val binding: ItemRatingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(rating: Rating) {
            binding.tvRaterName.text = rating.rater.profileName
            binding.tvRatingDescription.text = rating.description
            binding.ratingBar.rating = rating.rating.toFloat()

            Glide.with(binding.ivRaterProfile.context)
                .load("https://www.bumpy-insects-reply-yearly.a276.dcdg.xyz"+rating.rater.profilePicture)
                .into(binding.ivRaterProfile)

            val firstAttachmentPath = "https://www.bumpy-insects-reply-yearly.a276.dcdg.xyz"+rating.attachments?.firstOrNull()?.path
            firstAttachmentPath?.let {
                Glide.with(binding.ivAttachment.context)
                    .load(it)
                    .into(binding.ivAttachment)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingViewHolder {
        val binding = ItemRatingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RatingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RatingViewHolder, position: Int) {
        holder.bind(ratings[position])
    }

    override fun getItemCount(): Int = ratings.size

    fun updateRatings(newRatings: List<Rating>) {
        ratings = newRatings
        notifyDataSetChanged()
    }
}