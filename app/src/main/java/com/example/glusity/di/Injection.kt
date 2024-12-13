package com.example.glusity.di

import android.content.Context
import com.example.glusity.api.ApiClient
import com.example.glusity.data.UserRepository

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiClient.getApiService(context)
        return UserRepository(apiService)
    }
}