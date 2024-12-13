package com.example.glusity.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.example.glusity.databinding.ActivityWelcomeBinding
import com.example.glusity.login.LoginActivity
import com.example.glusity.signup.SignUpActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupActions()
        playAnimations()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupActions() {
        binding.loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.signupButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun playAnimations() {
        val imageAlphaAnimator = ObjectAnimator.ofFloat(binding.imageView, "alpha", 0f, 1f).apply {
            duration = 1000
            interpolator = AccelerateDecelerateInterpolator()
        }

        val imageMoveAnimator = ObjectAnimator.ofFloat(binding.imageView, "translationX", -50f, 50f).apply {
            duration = 1000
            interpolator = AccelerateDecelerateInterpolator()
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }

        val loginButtonAnimator = ObjectAnimator.ofFloat(binding.loginButton, "translationY", 200f, 0f).apply {
            duration = 800
            interpolator = AccelerateDecelerateInterpolator()
        }

        val signupButtonAnimator = ObjectAnimator.ofFloat(binding.signupButton, "translationY", 200f, 0f).apply {
            duration = 800
            interpolator = AccelerateDecelerateInterpolator()
        }
        AnimatorSet().apply {
            playTogether(imageAlphaAnimator, imageMoveAnimator, loginButtonAnimator, signupButtonAnimator)
            start()
        }
    }
}