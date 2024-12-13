package com.example.glusity.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.glusity.data.UserRepository
import com.example.glusity.response.RegisterResponse
import com.example.glusity.response.Result

class SignupViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun register(name: String, email: String, password: String, confirmPassword: String): LiveData<Result<RegisterResponse>> {
        return userRepository.postSignUp(name, email, password, confirmPassword)
    }
}