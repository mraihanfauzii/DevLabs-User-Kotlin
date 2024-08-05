package com.hackathon.devlabsuser.adapter.home.message

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hackathon.devlabsuser.databinding.ItemMessageBinding
import com.hackathon.devlabsuser.model.Message
import com.hackathon.devlabsuser.utils.DateUtils

class MessageAdapter(private var messages: List<Message>) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    inner class MessageViewHolder(private val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.tvMessage.text = message.message
            binding.tvTimestamp.text = DateUtils.formatTimestamp(message.createdAt)
            binding.tvUserName.text = message.sender.profileName
            Glide.with(binding.ivUserProfile.context)
                .load("https://www.bumpy-insects-reply-yearly.a276.dcdg.xyz" + message.sender.profilePicture)
                .into(binding.ivUserProfile)
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

    fun updateMessages(newMessages: List<Message>) {
        messages = newMessages
        notifyDataSetChanged()
    }
}