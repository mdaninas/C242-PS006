package com.example.glusity.response

data class LoginResponse(
    val message: String,
    val accessToken: String,
    val refreshToken: String,
    val userId: Int
)