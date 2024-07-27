package com.hackathon.devlabsuser.api

import com.hackathon.devlabsuser.model.AddMessageRequest
import com.hackathon.devlabsuser.model.AddMessageResponse
import com.hackathon.devlabsuser.model.AddPortfolioRequest
import com.hackathon.devlabsuser.model.AddPortfolioResponse
import com.hackathon.devlabsuser.model.DeleteResponse
import com.hackathon.devlabsuser.model.GetMessageResponse
import com.hackathon.devlabsuser.model.GetPortfolioResponse
import com.hackathon.devlabsuser.model.GetProfileResponse
import com.hackathon.devlabsuser.model.GetPromoResponse
import com.hackathon.devlabsuser.model.LoginRequest
import com.hackathon.devlabsuser.model.LoginResponse
import com.hackathon.devlabsuser.model.RegisterRequest
import com.hackathon.devlabsuser.model.RegisterResponse
import com.hackathon.devlabsuser.model.UpdateProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @POST("users/register")
    fun register (
        @Body registerRequest: RegisterRequest
    ): Call<RegisterResponse>

    @POST("users/login")
    fun login (
        @Body loginRequest: LoginRequest
    ): Call<LoginResponse>

    @DELETE("users/logout")
    fun logout (
        @Header("Authorization") token: String
    ): Call<DeleteResponse>

    @GET("users")
    fun getProfile (
        @Header("Authorization") token: String,
    ): Call<GetProfileResponse>

    @Multipart
    @PUT("users")
    fun putProfile (
        @Header("Authorization") token: String,
        @Part("profile_name") profileName: RequestBody,
        @Part("profile_description") profileDescription: RequestBody,
        @Part("phonenumber") phoneNumber: RequestBody,
        @Part profile_picture: MultipartBody.Part
    ): Call<UpdateProfileResponse>

    @GET("users/list")
    fun getArchitect (
        @Header("Authorization") token: String
    ): Call<GetProfileResponse>

    @GET("promo")
    fun getPromo (
        @Header("Authorization") token: String,
    ): Call<GetPromoResponse>

    @POST("portofolios")
    fun addPortfolio (
        @Header("Authorization") token: String,
        @Body addPortfolioRequest: AddPortfolioRequest
    ): Call<AddPortfolioResponse>

    @GET("portofolios")
    fun getPortfolio (
        @Header("Authorization") token: String,
        @Query("architect_id") architectId: String,
    ): Call<GetPortfolioResponse>

    @Multipart
    @PUT("portofolios")
    fun putPortfolio (
        @Header("Authorization") token: String,
        @Part("name") profileDescription: RequestBody,
        @Part("description") phoneNumber: RequestBody,
        @Part attachment_files: MultipartBody.Part
    )

    @DELETE("portofolios")
    fun deletePortfolio (
        @Header("Authorization") token: String,
        @Query("architect_id") architectId: String,
    ): Call<DeleteResponse>

    @GET("users/login")
    fun getMessage (
        @Header("Authorization") token: String,
        @Query("first_user_id") firstUserId: String,
        @Query("second_user_id") secondUserId: String,
    ): Call<GetMessageResponse>

    @POST("users/login")
    fun addMessage (
        @Header("Authorization") token: String,
        @Body addMessageRequest: AddMessageRequest
    ): Call<AddMessageResponse>
}