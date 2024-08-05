package com.hackathon.devlabsuser.ui.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hackathon.devlabsuser.R
import com.hackathon.devlabsuser.adapter.home.message.MessageAdapter
import com.hackathon.devlabsuser.databinding.ActivityMessageBinding
import com.hackathon.devlabsuser.model.AddMessageRequest
import com.hackathon.devlabsuser.ui.main.home.DetailArticleActivity
import com.hackathon.devlabsuser.viewmodel.MessageViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MessageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMessageBinding
    private val messageViewModel: MessageViewModel by viewModels()
    private lateinit var messageAdapter: MessageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val architectId = intent.getStringExtra("architect_id")
        val userId = intent.getStringExtra("user_id")
        val token = intent.getStringExtra("token")
        messageAdapter = MessageAdapter(emptyList())
        binding.rvMessages.layoutManager = LinearLayoutManager(this)
        binding.rvMessages.adapter = messageAdapter

        messageViewModel.isLoading.observe(this, Observer { isLoading ->
            showLoading(isLoading)
        })

        messageViewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

        messageViewModel.getMessage.observe(this, Observer { messages ->
            messageAdapter.updateMessages(messages.data)
            binding.rvMessages.scrollToPosition(messages.data.size - 1)
        })

        if (architectId != null && userId != null) {
            messageViewModel.getMessage(token!!, userId, architectId)
            messageViewModel.viewModelScope.launch {
                while (true) {
                    messageViewModel.startFetchingMessages(token, userId, architectId)
                    delay(10000) // Fetch new messages every 10 seconds
                }
            }

            binding.btnSend.setOnClickListener {
                val messageText = binding.etMessage.text.toString()
                if (messageText.isNotBlank()) {
                    val addMessageRequest = AddMessageRequest(receiverId = architectId, message = messageText)
                    messageViewModel.addMessage(token, addMessageRequest)
                } else {
                    Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}