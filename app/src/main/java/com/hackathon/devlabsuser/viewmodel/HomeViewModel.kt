package com.hackathon.devlabsuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hackathon.devlabsuser.api.ApiConfig
import com.hackathon.devlabsuser.model.ApiResponse
import com.hackathon.devlabsuser.model.ProjectRequest
import com.hackathon.devlabsuser.model.ProjectResponse
import com.hackathon.devlabsuser.model.Promo
import com.hackathon.devlabsuser.model.RecArchitect
import com.hackathon.devlabsuser.model.RecommendedArchitectsRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _promo = MutableLiveData<List<Promo>>()
    val promo : LiveData<List<Promo>> = _promo

    private val _projectResponse = MutableLiveData<ProjectResponse>()
    val projectResponse: LiveData<ProjectResponse> get() = _projectResponse

    private val _recArchitectResponse = MutableLiveData<List<RecArchitect>>()
    val recArchitectResponse: LiveData<List<RecArchitect>> get() = _recArchitectResponse

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

    fun submitProject(token: String, userId: String, projectRequest: ProjectRequest) {
        _isLoading.value = true
        ApiConfig.getApiService().createProject(token, userId, projectRequest).enqueue(object :
            Callback<ProjectResponse> {
            override fun onResponse(call: Call<ProjectResponse>, response: Response<ProjectResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _projectResponse.value = response.body()
                } else {
                    Log.e("HomeViewModel", "Failed to submit project: ${response.code()}")
                    _errorMessage.value = "Failed to submit project"
                }
            }

            override fun onFailure(call: Call<ProjectResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("HomeViewModel", "Failed to submit project: ${t.message}")
                _errorMessage.value = "Failed to submit project"
            }
        })
    }

    fun getRecommendedArchitects(token: String,request: RecommendedArchitectsRequest) {
        _isLoading.value = true // Tampilkan indikator loading
        ApiConfig.getApiService().getRecommendedArchitects(token ,request).enqueue(object : Callback<ApiResponse<List<RecArchitect>>> {
            override fun onResponse(call: Call<ApiResponse<List<RecArchitect>>>, response: Response<ApiResponse<List<RecArchitect>>>) {
                _isLoading.value = false // Sembunyikan indikator loading
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.success == true) {
                        _recArchitectResponse.value = apiResponse.data
                    } else {
                        _errorMessage.value = apiResponse?.message ?: "Terjadi kesalahan."
                    }
                } else {
                    _errorMessage.value = "Gagal mengambil data. Kode error: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<RecArchitect>>>, t: Throwable) {
                _isLoading.value = false // Sembunyikan indikator loading
                _errorMessage.value = "Terjadi kesalahan jaringan. Silakan coba lagi nanti."
                t.printStackTrace() // Untuk keperluan debugging
            }
        })
    }
}