package com.example.glusity.signup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.glusity.api.ApiClient
import com.example.glusity.data.UserRepository
import com.example.glusity.databinding.ActivitySignUpBinding
import com.example.glusity.response.Result
import com.example.glusity.login.LoginActivity
import com.example.glusity.welcome.WelcomeActivity

class SignUpActivity : AppCompatActivity() {
    private lateinit var viewModel: SignupViewModel
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val apiService = ApiClient.getApiService(this)
        val repository = UserRepository(apiService)
        val factory = SignupViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[SignupViewModel::class.java]

        binding.signupButton.setOnClickListener {
            val name = binding.fullNameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.register(name, email, password, confirmPassword).observe(this) { result ->
                    when (result) {
                        is Result.Loading -> {
                            Toast.makeText(this, "Processing...", Toast.LENGTH_SHORT).show()
                        }

                        is Result.Success -> {
                            Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT)
                                .show()
                            navigateToLoginActivity()
                        }

                        is Result.Error -> {
                            Toast.makeText(this, "Error: ${result.message}", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginTextView.setOnClickListener {
            navigateToLoginActivity()
        }

        binding.backButton.setOnClickListener {
            navigateToWelcomeActivity()
        }
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToWelcomeActivity() {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}