package com.example.glusity.ui.splash

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.example.glusity.data.UserPreference
import com.example.glusity.databinding.ActivitySplashBinding
import com.example.glusity.main.MainActivity
import com.example.glusity.welcome.WelcomeActivity

class SplashActivity : AppCompatActivity() {
  private lateinit var binding: ActivitySplashBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivitySplashBinding.inflate(layoutInflater)
    setContentView(binding.root)
    supportActionBar?.hide()

    playAnimations()

    if (UserPreference.isLoggedIn(this)) {
      navigateToMainActivity()
    }

    binding.logoutButton.setOnClickListener {
      navigateToWelcomeActivity()
    }
  }

  private fun navigateToMainActivity() {
    val intent = Intent(this, MainActivity::class.java)
    startActivity(intent)
    finish()
  }

  private fun navigateToWelcomeActivity() {
    val intent = Intent(this, WelcomeActivity::class.java)
    startActivity(intent)
    finish()
  }

  private fun playAnimations() {
    val logoAlphaAnimator = ObjectAnimator.ofFloat(binding.logoImageView, "alpha", 0f, 1f).apply {
      duration = 1000
      interpolator = AccelerateDecelerateInterpolator()
    }

    val logoScaleXAnimator = ObjectAnimator.ofFloat(binding.logoImageView, "scaleX", 0.8f, 1f).apply {
      duration = 1000
      interpolator = AccelerateDecelerateInterpolator()
    }

    val logoScaleYAnimator = ObjectAnimator.ofFloat(binding.logoImageView, "scaleY", 0.8f, 1f).apply {
      duration = 1000
      interpolator = AccelerateDecelerateInterpolator()
    }

    val buttonAnimator = ObjectAnimator.ofFloat(binding.logoutButton, "translationY", 200f, 0f).apply {
      duration = 800
      interpolator = AccelerateDecelerateInterpolator()
    }

    AnimatorSet().apply {
      playTogether(logoAlphaAnimator, logoScaleXAnimator, logoScaleYAnimator, buttonAnimator)
      start()
    }
  }
}