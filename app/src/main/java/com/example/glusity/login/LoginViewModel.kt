package com.example.glusity.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.glusity.data.UserRepository
import com.example.glusity.response.LoginResponse
import com.example.glusity.response.Result

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun login(email: String, password: String): LiveData<Result<LoginResponse>> {
        return userRepository.postLogin(email, password)
    }
}