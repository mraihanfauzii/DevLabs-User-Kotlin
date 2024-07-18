package com.hackathon.devlabsuser.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hackathon.devlabsuser.api.ApiConfig
import com.hackathon.devlabsuser.model.AddPortfolioRequest
import com.hackathon.devlabsuser.model.AddPortfolioResponse
import com.hackathon.devlabsuser.model.DeleteResponse
import com.hackathon.devlabsuser.model.GetPortfolioResponse
import com.hackathon.devlabsuser.model.LoginRequest
import com.hackathon.devlabsuser.model.LoginResponse
import com.hackathon.devlabsuser.model.RegisterRequest
import com.hackathon.devlabsuser.model.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PortfolioViewModel(application: Application) : AndroidViewModel(application) {
    private val _addPortfolio = MutableLiveData<AddPortfolioResponse>()
    val addPortfolio : LiveData<AddPortfolioResponse> = _addPortfolio

    private val _getPortfolio = MutableLiveData<GetPortfolioResponse>()
    val getPortfolio : LiveData<GetPortfolioResponse> = _getPortfolio

    private val _deletePortfolio = MutableLiveData<DeleteResponse>()
    val deletePortfolio : LiveData<DeleteResponse> = _deletePortfolio

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun addPortfolio(token: String, addPortfolioRequest: AddPortfolioRequest){
        _isLoading.value = true
        ApiConfig.getApiService().addPortfolio(token, addPortfolioRequest).enqueue(object :
            Callback<AddPortfolioResponse> {
            override fun onResponse(
                call: Call<AddPortfolioResponse>,
                response: Response<AddPortfolioResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _addPortfolio.value = response.body()
                } else {
                    Log.e("RegisterViewModel", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AddPortfolioResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("RegisterViewModel", "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getPortfolio(token: String, architectId: String){
        _isLoading.value = true
        ApiConfig.getApiService().getPortfolio(token, architectId).enqueue(object : Callback<GetPortfolioResponse> {
            override fun onResponse(
                call: Call<GetPortfolioResponse>,
                response: Response<GetPortfolioResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _getPortfolio.value = response.body()
                } else {
                    Log.e("LoginViewModel", "onFailure: ${response.message()}")
                    _errorMessage.value = "Failed to load Product"
                }
            }

            override fun onFailure(call: Call<GetPortfolioResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("LoginViewModel", "onFailure: ${t.message.toString()}")
                _errorMessage.value = "Error, Failed to load Product"
            }
        })
    }

    fun deleteReason(token: String, architectId: String){
        _isLoading.value = true
        ApiConfig.getApiService().deletePortfolio(token, architectId).enqueue(object :
            Callback<DeleteResponse> {
            override fun onResponse(
                call: Call<DeleteResponse>,
                response: Response<DeleteResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _deletePortfolio.value = response.body()
                } else {
                    Log.e("PortfolioViewModel", "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("PortfolioViewModel", "onFailure: ${t.message.toString()}")
            }
        })
    }
}