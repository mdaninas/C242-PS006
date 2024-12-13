package com.example.glusity.ui.setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.glusity.data.UserPreference
import com.example.glusity.databinding.FragmentSettingBinding
import com.example.glusity.ui.splash.SplashActivity

class SettingFragment : Fragment() {
  private var _binding: FragmentSettingBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View? {
    _binding = FragmentSettingBinding.inflate(inflater, container, false)
    val root: View = binding.root

    binding.logoutContainer.setOnClickListener {
      logOut()
    }

    return root
  }

  private fun logOut() {
    context?.let { UserPreference.logOut(it) }

    val intent = Intent(activity, SplashActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}