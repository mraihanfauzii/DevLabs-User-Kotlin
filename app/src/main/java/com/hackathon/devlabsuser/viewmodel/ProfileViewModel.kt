package com.hackathon.devlabsuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hackathon.devlabsuser.api.ApiConfig
import com.hackathon.devlabsuser.model.ApiResponse
import com.hackathon.devlabsuser.model.UpdateProfileData
import com.hackathon.devlabsuser.model.UserData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel : ViewModel() {
    private val _getProfileResponse = MutableLiveData<UserData>()
    val getProfileResponse : LiveData<UserData> = _getProfileResponse
    private val _putProfileResponse = MutableLiveData<ApiResponse<UpdateProfileData>>()
    val putProfileResponse : LiveData<ApiResponse<UpdateProfileData>> = _putProfileResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun getProfile(token: String){
        _isLoading.value = true
        ApiConfig.getApiService().getProfile(token).enqueue(object :
            Callback<ApiResponse<UserData>> {
            override fun onResponse(
                call: Call<ApiResponse<UserData>>,
                response: Response<ApiResponse<UserData>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _getProfileResponse.value = response.body()?.data
                } else {
                    Log.e("ProfileViewModel", "onFailure: ${response.message()}")
                    _getProfileResponse.value = response.body()?.data
                    _errorMessage.value = "Failed to get profile"
                }
            }

            override fun onFailure(call: Call<ApiResponse<UserData>>, t: Throwable) {
                _isLoading.value = false
                Log.e("ProfileViewModel", "onFailure: ${t.message.toString()}")
                _errorMessage.value = "Failed to get profile"
            }
        })
    }

    fun putProfile(token: String, profileName: RequestBody, profileDescription: RequestBody, phoneNumber: RequestBody, profile_picture: MultipartBody.Part){
        _isLoading.value = true
        ApiConfig.getApiService().putProfile(token, profileName, profileDescription, phoneNumber, profile_picture).enqueue(object :
            Callback<ApiResponse<UpdateProfileData>> {
            override fun onResponse(
                call: Call<ApiResponse<UpdateProfileData>>,
                response: Response<ApiResponse<UpdateProfileData>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _putProfileResponse.value = response.body()
                } else {
                    Log.e("ProfileViewModel", "onFailure: ${response.message()}")
                    _putProfileResponse.value = response.body()
                    _errorMessage.value = "Failed to get profile"
                }
            }

            override fun onFailure(call: Call<ApiResponse<UpdateProfileData>>, t: Throwable) {
                _isLoading.value = false
                Log.e("ProfileViewModel", "onFailure: ${t.message.toString()}")
                _errorMessage.value = "Failed to get profile"
            }
        })
    }
}