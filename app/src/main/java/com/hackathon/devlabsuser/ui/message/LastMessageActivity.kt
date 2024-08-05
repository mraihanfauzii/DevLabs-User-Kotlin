package com.hackathon.devlabsuser.ui.message

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hackathon.devlabsuser.adapter.home.message.LastMessageAdapter
import com.hackathon.devlabsuser.databinding.ActivityChatBinding
import com.hackathon.devlabsuser.model.LastMessage
import com.hackathon.devlabsuser.ui.authentication.AuthenticationManager
import com.hackathon.devlabsuser.ui.main.home.DetailArticleActivity
import com.hackathon.devlabsuser.viewmodel.MessageViewModel

class LastMessageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var authenticationManager: AuthenticationManager
    private val messageViewModel: MessageViewModel by viewModels()
    private lateinit var lastMessageAdapter: LastMessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authenticationManager = AuthenticationManager(this)
        lastMessageAdapter = LastMessageAdapter(emptyList())
        binding.rvChat.layoutManager = LinearLayoutManager(this)
        binding.rvChat.adapter = lastMessageAdapter

        messageViewModel.isLoading.observe(this, Observer { isLoading ->
            showLoading(isLoading)
        })

        messageViewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

        messageViewModel.getLastMessage.observe(this, Observer { messages ->
            lastMessageAdapter.updateMessages(messages.data)
            binding.rvChat.scrollToPosition(messages.data.size - 1)
        })

        val userId = authenticationManager.getAccess(AuthenticationManager.ID).toString()
        val getToken = authenticationManager.getAccess(AuthenticationManager.TOKEN).toString()
        val token = "Bearer $getToken"
        messageViewModel.startFetchingLastMessages(token)

        lastMessageAdapter.setOnItemClickCallback(object: LastMessageAdapter.OnItemClickCallback {
            override fun onItemClicked(message: LastMessage) {
                Intent(this@LastMessageActivity, MessageActivity::class.java).also {
                    it.putExtra("architect_id", message.contact.id)
                    it.putExtra("user_id", userId)
                    it.putExtra("token", token)
                    startActivity(it)
                }
            }
        })
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}