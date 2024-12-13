package com.example.glusity.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.glusity.api.ApiClient
import com.example.glusity.data.UserPreference
import com.example.glusity.data.UserRepository
import com.example.glusity.databinding.ActivityLoginBinding
import com.example.glusity.response.Result
import com.example.glusity.signup.SignUpActivity
import com.example.glusity.success.SuccessActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val apiService = ApiClient.getApiService(this)
        val repository = UserRepository(apiService)
        val factory = LoginViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        binding.backButton.setOnClickListener {
            navigateToSignUpActivity()
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(email, password).observe(this) { result ->
                    when (result) {
                        is Result.Loading -> {
                            Toast.makeText(this, "Processing...", Toast.LENGTH_SHORT).show()
                        }

                        is Result.Success -> {
                            val response = result.data
                            UserPreference.saveSessionAndToken(response.accessToken, true, this)
                            UserPreference.saveRefreshToken(response.refreshToken, this)
                            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                            navigateToSuccessActivity()
                            val token = result.data.accessToken

                        }

                        is Result.Error -> {
                            Toast.makeText(this, "Error: ${result.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.signUpTextView.setOnClickListener {
            navigateToSignUpActivity()
        }
    }

    private fun navigateToSuccessActivity() {
        val intent = Intent(this, SuccessActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToSignUpActivity() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
        finish()
    }
}