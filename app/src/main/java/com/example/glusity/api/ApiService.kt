package com.example.glusity.api

import com.example.glusity.data.ObesityPredictionRequest
import com.example.glusity.response.DiabetesPredictionRequest
import com.example.glusity.response.DiabetesPredictionResponse
import com.example.glusity.response.LoginRequest
import com.example.glusity.response.LoginResponse
import com.example.glusity.response.RegisterRequest
import com.example.glusity.response.RegisterResponse
import com.example.glusity.ui.history.HistoryItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("auth/register")
    suspend fun postSignUp(
        @Body request: RegisterRequest
    ): RegisterResponse

    @POST("auth/login")
    suspend fun postLogin(
        @Body request: LoginRequest
    ): LoginResponse

    @POST("auth/refresh")
    suspend fun postRefreshToken(
        @Body refreshToken:
        Map<String, String>): LoginResponse

    @POST("predict/diabetes")
    suspend fun predictDiabetes(@Body request: DiabetesPredictionRequest): DiabetesPredictionResponse

    @POST("predict/save")
    suspend fun savePrediction(@Body predictionData: Map<String, Any>): Response<Void>

    @POST("predict/obesity")
    suspend fun predictObesity(@Body request: ObesityPredictionRequest): Map<String, Any>

    @GET("predict/history")
    suspend fun getPredictionHistory(): List<HistoryItem>

    @GET("predict/history/diabetes/{userId}")
    suspend fun getDiabetesHistory(@Path("userId") userId: Int): List<HistoryItem>

    @GET("predict/history/obesity/{userId}")
    suspend fun getObesityHistory(@Path("userId") userId: Int): List<HistoryItem>
}