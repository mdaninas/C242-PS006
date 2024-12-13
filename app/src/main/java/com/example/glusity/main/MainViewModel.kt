package com.example.glusity.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.glusity.data.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun logout() {
        viewModelScope.launch {

        }
    }
}