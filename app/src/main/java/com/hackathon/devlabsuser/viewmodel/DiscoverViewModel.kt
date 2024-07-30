package com.hackathon.devlabsuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hackathon.devlabsuser.api.ApiConfig
import com.hackathon.devlabsuser.model.ApiResponse
import com.hackathon.devlabsuser.model.Portfolio
import com.hackathon.devlabsuser.model.Theme
import com.hackathon.devlabsuser.model.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiscoverViewModel() : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _getAllArchitect = MutableLiveData<List<UserData>>()
    val getAllArchitect : LiveData<List<UserData>> = _getAllArchitect

    private val _getAllThemes = MutableLiveData<List<Theme>>()
    val getAllThemes : LiveData<List<Theme>> = _getAllThemes

    private val _getTrendingPortfolios = MutableLiveData<List<Portfolio>>()
    val getTrendingPortfolios : LiveData<List<Portfolio>> = _getTrendingPortfolios

    private val _getRecentPortfolios = MutableLiveData<List<Portfolio>>()
    val getRecentPortfolios : LiveData<List<Portfolio>> = _getRecentPortfolios

    fun getAllArchitect(token: String){
        _isLoading.value = true
        ApiConfig.getApiService().getAllUsers(token).enqueue(object :
            Callback<ApiResponse<List<UserData>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<UserData>>>,
                response: Response<ApiResponse<List<UserData>>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val allUsers = response.body()?.data ?: emptyList()
                    val architects = allUsers.filter { it.role == "architect" }
                    _getAllArchitect.value = response.body()?.data?.filter { it.role == "architect" }
                } else {
                    Log.e("PromoViewModel", "onFailure: ${response.message()}")
                    _getAllArchitect.value = response.body()?.data
                    _errorMessage.value = "Failed to get promo"
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<UserData>>>, t: Throwable) {
                _isLoading.value = false
                Log.e("PromoViewModel", "onFailure: ${t.message.toString()}")
                _errorMessage.value = "Failed to get promo"
            }
        })
    }

    fun getAllThemes(token: String){
        _isLoading.value = true
        ApiConfig.getApiService().getAllThemes(token).enqueue(object :
            Callback<ApiResponse<List<Theme>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Theme>>>,
                response: Response<ApiResponse<List<Theme>>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _getAllThemes.value = response.body()?.data
                } else {
                    Log.e("PromoViewModel", "onFailure: ${response.message()}")
                    _getAllThemes.value = response.body()?.data
                    _errorMessage.value = "Failed to get promo"
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Theme>>>, t: Throwable) {
                _isLoading.value = false
                Log.e("PromoViewModel", "onFailure: ${t.message.toString()}")
                _errorMessage.value = "Failed to get promo"
            }
        })
    }

    fun getTrendingPortfolios(token: String){
        _isLoading.value = true
        ApiConfig.getApiService().getTrendingPortfolios(token).enqueue(object :
            Callback<ApiResponse<List<Portfolio>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Portfolio>>>,
                response: Response<ApiResponse<List<Portfolio>>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _getTrendingPortfolios.value = response.body()?.data
                } else {
                    Log.e("PromoViewModel", "onFailure: ${response.message()}")
                    _getTrendingPortfolios.value = response.body()?.data
                    _errorMessage.value = "Failed to get promo"
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Portfolio>>>, t: Throwable) {
                _isLoading.value = false
                Log.e("PromoViewModel", "onFailure: ${t.message.toString()}")
                _errorMessage.value = "Failed to get promo"
            }
        })
    }

    fun getRecentPortfolios(token: String){
        _isLoading.value = true
        ApiConfig.getApiService().getRecentPortfolios(token).enqueue(object :
            Callback<ApiResponse<List<Portfolio>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Portfolio>>>,
                response: Response<ApiResponse<List<Portfolio>>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _getRecentPortfolios.value = response.body()?.data
                } else {
                    Log.e("PromoViewModel", "onFailure: ${response.message()}")
                    _getRecentPortfolios.value = response.body()?.data
                    _errorMessage.value = "Failed to get promo"
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Portfolio>>>, t: Throwable) {
                _isLoading.value = false
                Log.e("PromoViewModel", "onFailure: ${t.message.toString()}")
                _errorMessage.value = "Failed to get promo"
            }
        })
    }
}