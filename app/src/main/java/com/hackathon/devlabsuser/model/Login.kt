package com.hackathon.devlabsuser.model

data class LoginRequest(
    val email: String,
    val password: String,
    val role: String
)

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val code: Int,
    val data: LoginData
)

data class LoginData(
    val accessToken: String
)