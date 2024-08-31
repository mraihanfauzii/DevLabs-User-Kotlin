package com.hackathon.devlabsuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hackathon.devlabsuser.api.ApiConfig
import com.hackathon.devlabsuser.model.ApiResponse
import com.hackathon.devlabsuser.model.Project
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProgressViewModel : ViewModel() {

    private val _projectsConfirm = MutableLiveData<List<Project>>()
    val projectsConfirm: LiveData<List<Project>> = _projectsConfirm

    private val _projectsProgress = MutableLiveData<List<Project>>()
    val projectsProgress: LiveData<List<Project>> = _projectsProgress

    private val _projectsDone = MutableLiveData<List<Project>>()
    val projectsDone: LiveData<List<Project>> = _projectsDone

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun getProjectsByUserIdConfirm(token: String){
        _isLoading.value = true
        ApiConfig.getApiService().getProjectsByUserId(token).enqueue(object :
            Callback<ApiResponse<List<Project>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Project>>>,
                response: Response<ApiResponse<List<Project>>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val allProjects = response.body()?.data
                    // Filter proyek dengan status "Menunggu konfirmasi"
                    val filteredProjects = allProjects?.filter { it.status == "Menunggu konfirmasi" }
                    _projectsConfirm.value = filteredProjects
                } else {
                    Log.e("ProfileViewModel", "onFailure: ${response.message()}")
                    _projectsConfirm.value = response.body()?.data
                    _errorMessage.value = "Failed to get profile"
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Project>>>, t: Throwable) {
                _isLoading.value = false
                Log.e("ProfileViewModel", "onFailure: ${t.message.toString()}")
                _errorMessage.value = "Failed to get profile"
            }
        })
    }

    fun getProjectsByUserIdProgress(token: String){
        _isLoading.value = true
        ApiConfig.getApiService().getProjectsByUserId(token).enqueue(object :
            Callback<ApiResponse<List<Project>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Project>>>,
                response: Response<ApiResponse<List<Project>>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val allProjects = response.body()?.data
                    // Filter proyek dengan status "Menunggu konfirmasi"
                    val filteredProjects = allProjects?.filter { it.status != "Menunggu konfirmasi" && it.status != "Selesai" }
                    _projectsProgress.value = filteredProjects
                } else {
                    Log.e("ProfileViewModel", "onFailure: ${response.message()}")
                    _projectsProgress.value = response.body()?.data
                    _errorMessage.value = "Failed to get profile"
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Project>>>, t: Throwable) {
                _isLoading.value = false
                Log.e("ProfileViewModel", "onFailure: ${t.message.toString()}")
                _errorMessage.value = "Failed to get profile"
            }
        })
    }

    fun getProjectsByUserIdDone(token: String){
        _isLoading.value = true
        ApiConfig.getApiService().getProjectsByUserId(token).enqueue(object :
            Callback<ApiResponse<List<Project>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Project>>>,
                response: Response<ApiResponse<List<Project>>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val allProjects = response.body()?.data
                    // Filter proyek dengan status "Menunggu konfirmasi"
                    val filteredProjects = allProjects?.filter { it.status == "Selesai" }
                    _projectsDone.value = filteredProjects
                } else {
                    Log.e("ProfileViewModel", "onFailure: ${response.message()}")
                    _projectsDone.value = response.body()?.data
                    _errorMessage.value = "Failed to get profile"
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Project>>>, t: Throwable) {
                _isLoading.value = false
                Log.e("ProfileViewModel", "onFailure: ${t.message.toString()}")
                _errorMessage.value = "Failed to get profile"
            }
        })
    }
}