package com.example.glusity.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.glusity.api.ApiService
import com.example.glusity.response.LoginRequest
import com.example.glusity.response.LoginResponse
import com.example.glusity.response.RegisterRequest
import com.example.glusity.response.RegisterResponse
import com.example.glusity.response.Result
import kotlinx.coroutines.Dispatchers

class UserRepository(private val apiService: ApiService) {

    fun postSignUp(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): LiveData<Result<RegisterResponse>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val request = RegisterRequest(name, email, password, confirmPassword)
            val response = apiService.postSignUp(request)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.e("UserRepository", "postSignUp: ${e.message}", e)
            emit(Result.Error(e.message ?: "Unknown Error"))
        }
    }

    fun postLogin(
        email: String,
        password: String
    ): LiveData<Result<LoginResponse>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val request = LoginRequest(email, password)
            val response = apiService.postLogin(request)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.e("UserRepository", "postLogin: ${e.message}", e)
            emit(Result.Error(e.message ?: "Unknown Error"))
        }
        fun clearSession(context: Context) {
            UserPreference.logOut(context)
        }
    }
}