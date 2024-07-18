package com.hackathon.devlabsuser.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hackathon.devlabsuser.api.ApiConfig
import com.hackathon.devlabsuser.model.AddMessageRequest
import com.hackathon.devlabsuser.model.AddMessageResponse
import com.hackathon.devlabsuser.model.GetMessageResponse
import com.hackathon.devlabsuser.model.LoginRequest
import com.hackathon.devlabsuser.model.LoginResponse
import com.hackathon.devlabsuser.model.RegisterRequest
import com.hackathon.devlabsuser.model.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessageViewModel(application: Application) : AndroidViewModel(application) {
    private val _addMessage = MutableLiveData<AddMessageResponse>()
    val addMessage : LiveData<AddMessageResponse> = _addMessage

    private val _getMessage = MutableLiveData<GetMessageResponse>()
    val getMessage : LiveData<GetMessageResponse> = _getMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun addMessage(token: String, addMessageRequest: AddMessageRequest){
        _isLoading.value = true
        ApiConfig.getApiService().addMessage(token, addMessageRequest).enqueue(object :
            Callback<AddMessageResponse> {
            override fun onResponse(
                call: Call<AddMessageResponse>,
                response: Response<AddMessageResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _addMessage.value = response.body()
                } else {
                    Log.e("RegisterViewModel", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AddMessageResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("RegisterViewModel", "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getMessage(token: String, firstUserId: String, secondUserId: String){
        _isLoading.value = true
        ApiConfig.getApiService().getMessage(token, firstUserId, secondUserId).enqueue(object : Callback<GetMessageResponse> {
            override fun onResponse(
                call: Call<GetMessageResponse>,
                response: Response<GetMessageResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _getMessage.value = response.body()
                } else {
                    Log.e("LoginViewModel", "onFailure: ${response.message()}")
                    _errorMessage.value = "Failed to load Product"
                }
            }

            override fun onFailure(call: Call<GetMessageResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("LoginViewModel", "onFailure: ${t.message.toString()}")
                _errorMessage.value = "Error, Failed to load Product"
            }
        })
    }
}