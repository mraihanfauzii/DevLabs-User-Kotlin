package com.hackathon.devlabsuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hackathon.devlabsuser.api.ApiConfig
import com.hackathon.devlabsuser.model.ApiResponse
import com.hackathon.devlabsuser.model.AverageRating
import com.hackathon.devlabsuser.model.Portfolio
import com.hackathon.devlabsuser.model.Rating
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArchitectViewModel : ViewModel() {
    private val _getPortfolio = MutableLiveData<ApiResponse<List<Portfolio>>>()
    val getPortfolio : LiveData<ApiResponse<List<Portfolio>>> = _getPortfolio

    private val _ratings = MutableLiveData<List<Rating>>()
    val ratings: LiveData<List<Rating>> = _ratings

    private val _ratingsAverage = MutableLiveData<AverageRating>()
    val ratingsAverage : LiveData<AverageRating> = _ratingsAverage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun getPortfolio(token: String, architectId: String){
        _isLoading.value = true
        ApiConfig.getApiService().getPortfolio(token, architectId).enqueue(object :
            Callback<ApiResponse<List<Portfolio>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Portfolio>>>,
                response: Response<ApiResponse<List<Portfolio>>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _getPortfolio.value = response.body()
                } else {
                    Log.e("LoginViewModel", "onFailure: ${response.message()}")
                    _errorMessage.value = "Failed to load Product"
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Portfolio>>>, t: Throwable) {
                _isLoading.value = false
                Log.e("LoginViewModel", "onFailure: ${t.message.toString()}")
                _errorMessage.value = "Error, Failed to load Product"
            }
        })
    }

    fun getRatings(token: String, architectId: String) {
        _isLoading.value = true
        ApiConfig.getApiService().getRatings(token, architectId).enqueue(object : Callback<ApiResponse<List<Rating>>> {
            override fun onResponse(call: Call<ApiResponse<List<Rating>>>, response: Response<ApiResponse<List<Rating>>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _ratings.value = response.body()?.data ?: emptyList()
                } else {
                    _errorMessage.value = response.message()
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Rating>>>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message
            }
        })
    }

    fun getRatingsAverage(token: String, architectId: String) {
        _isLoading.value = true
        ApiConfig.getApiService().getRatingsAverage(token, architectId).enqueue(object : Callback<ApiResponse<AverageRating>> {
            override fun onResponse(call: Call<ApiResponse<AverageRating>>, response: Response<ApiResponse<AverageRating>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _ratingsAverage.value = response.body()?.data
                } else {
                    _errorMessage.value = response.message()
                }
            }

            override fun onFailure(call: Call<ApiResponse<AverageRating>>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message
            }
        })
    }
}