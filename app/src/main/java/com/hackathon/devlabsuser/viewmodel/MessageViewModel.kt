package com.hackathon.devlabsuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackathon.devlabsuser.api.ApiConfig
import com.hackathon.devlabsuser.model.AddMessageData
import com.hackathon.devlabsuser.model.AddMessageRequest
import com.hackathon.devlabsuser.model.ApiResponse
import com.hackathon.devlabsuser.model.LastMessage
import com.hackathon.devlabsuser.model.Message
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessageViewModel : ViewModel() {
    private val _addMessage = MutableLiveData<ApiResponse<AddMessageData>>()
    val addMessage : LiveData<ApiResponse<AddMessageData>> = _addMessage

    private val _getLastMessage = MutableLiveData<ApiResponse<List<LastMessage>>>()
    val getLastMessage : LiveData<ApiResponse<List<LastMessage>>> = _getLastMessage

    private val _getMessage = MutableLiveData<ApiResponse<List<Message>>>()
    val getMessage : LiveData<ApiResponse<List<Message>>> = _getMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun addMessage(token: String, addMessageRequest: AddMessageRequest){
        _isLoading.value = true
        ApiConfig.getApiService().addMessage(token, addMessageRequest).enqueue(object :
            Callback<ApiResponse<AddMessageData>> {
            override fun onResponse(
                call: Call<ApiResponse<AddMessageData>>,
                response: Response<ApiResponse<AddMessageData>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _addMessage.value = response.body()
                } else {
                    Log.e("MessageViewModel", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<AddMessageData>>, t: Throwable) {
                _isLoading.value = false
                Log.e("MessageViewModel", "onFailure: ${t.message.toString()}")
            }
        })
    }

    private fun fetchLastMessages(token: String) {
        _isLoading.value = true
        ApiConfig.getApiService().getLastMessage(token).enqueue(object :
            Callback<ApiResponse<List<LastMessage>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<LastMessage>>>,
                response: Response<ApiResponse<List<LastMessage>>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _getLastMessage.value = response.body()
                } else {
                    Log.e("MessageViewModel", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<LastMessage>>>, t: Throwable) {
                _isLoading.value = false
                Log.e("MessageViewModel", "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun startFetchingLastMessages(token: String) {
        viewModelScope.launch {
            while (true) {
                fetchLastMessages(token)
                delay(10000) // Fetch new messages every 10 seconds
            }
        }
    }

    fun startFetchingMessages(token: String, firstUserId: String, secondUserId: String) {
        viewModelScope.launch {
            while (true) {
                getMessage(token, firstUserId, secondUserId)
                delay(10000) // Fetch new messages every 10 seconds
            }
        }
    }

    fun getMessage(token: String, firstUserId: String, secondUserId: String){
        _isLoading.value = true
        ApiConfig.getApiService().getMessage(token, firstUserId, secondUserId).enqueue(object : Callback<ApiResponse<List<Message>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Message>>>,
                response: Response<ApiResponse<List<Message>>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _getMessage.value = response.body()
                } else {
                    Log.e("MessageViewModel", "onFailure: ${response.message()}")
                    _errorMessage.value = "Failed to load messages"
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Message>>>, t: Throwable) {
                _isLoading.value = false
                Log.e("MessageViewModel", "onFailure: ${t.message.toString()}")
                _errorMessage.value = "Error, Failed to load messages"
            }
        })
    }
}