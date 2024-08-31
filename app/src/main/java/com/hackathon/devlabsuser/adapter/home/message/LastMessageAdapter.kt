package com.hackathon.devlabsuser.adapter.home.message

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hackathon.devlabsuser.R
import com.hackathon.devlabsuser.databinding.ItemMessageBinding
import com.hackathon.devlabsuser.model.LastMessage
import com.hackathon.devlabsuser.utils.DateUtils

class LastMessageAdapter(private var messages: List<LastMessage>) : RecyclerView.Adapter<LastMessageAdapter.MessageViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    inner class MessageViewHolder(private val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: LastMessage) {
            binding.apply {
                tvMessage.text = message.message
                tvTimestamp.text = DateUtils.formatTimestamp(message.createdAt)

                if (message.contact != null) {
                    tvUserName.text = message.contact.profileName ?: "Unknown"
                    Glide.with(ivUserProfile.context)
                        .load("https://www.bumpy-insects-reply-yearly.a276.dcdg.xyz"+message.contact.profilePicture)
                        .into(ivUserProfile)
                } else {
                    tvUserName.text = "Unknown"
                    ivUserProfile.setImageResource(R.drawable.ic_favorite_before)
                }

                root.setOnClickListener {
                    onItemClickCallback.onItemClicked(message)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount(): Int = messages.size

    fun updateMessages(newMessages: List<LastMessage>) {
        val oldSize = messages.size
        messages = newMessages
        notifyItemRangeInserted(oldSize, newMessages.size - oldSize)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(message: LastMessage)
    }
}