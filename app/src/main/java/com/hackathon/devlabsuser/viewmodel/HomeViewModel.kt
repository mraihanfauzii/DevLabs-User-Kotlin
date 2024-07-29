package com.hackathon.devlabsuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hackathon.devlabsuser.api.ApiConfig
import com.hackathon.devlabsuser.model.ApiResponse
import com.hackathon.devlabsuser.model.Promo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel() : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _promo = MutableLiveData<List<Promo>>()
    val promo : LiveData<List<Promo>> = _promo

    fun getPromo(token: String){
        _isLoading.value = true
        ApiConfig.getApiService().getPromo(token).enqueue(object :
            Callback<ApiResponse<List<Promo>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Promo>>>,
                response: Response<ApiResponse<List<Promo>>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _promo.value = response.body()?.data
                } else {
                    Log.e("PromoViewModel", "onFailure: ${response.message()}")
                    _promo.value = response.body()?.data
                    _errorMessage.value = "Failed to get promo"
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Promo>>>, t: Throwable) {
                _isLoading.value = false
                Log.e("PromoViewModel", "onFailure: ${t.message.toString()}")
                _errorMessage.value = "Failed to get promo"
            }
        })
    }
}