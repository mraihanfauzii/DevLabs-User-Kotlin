package com.hackathon.devlabsuser.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hackathon.devlabsuser.api.ApiConfig
import com.hackathon.devlabsuser.model.LoginRequest
import com.hackathon.devlabsuser.model.LoginResponse
import com.hackathon.devlabsuser.model.RegisterRequest
import com.hackathon.devlabsuser.model.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthenticationViewModel(application: Application) : AndroidViewModel(application) {
    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse : LiveData<RegisterResponse> = _registerResponse

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse : LiveData<LoginResponse> = _loginResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun register(registerRequest: RegisterRequest){
        _isLoading.value = true
        ApiConfig.getApiService().register(registerRequest).enqueue(object :
            Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _registerResponse.value = response.body()
                } else {
                    Log.e("RegisterViewModel", "onFailure: ${response.message()}")
                    _registerResponse.value = response.body()
                    _errorMessage.value = "Failed to register"
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("RegisterViewModel", "onFailure: ${t.message.toString()}")
                _errorMessage.value = "Failed to register"
            }
        })
    }

    fun login(loginRequest: LoginRequest){
        _isLoading.value = true
        ApiConfig.getApiService().login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _loginResponse.value = response.body()
                } else {
                    Log.e("LoginViewModel", "onFailure: ${response.message()}")
                    _loginResponse.value = response.body()
                    _errorMessage.value = "Failed to login"
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("LoginViewModel", "onFailure: ${t.message.toString()}")
                _errorMessage.value = "Failed to login"
            }
        })
    }
}