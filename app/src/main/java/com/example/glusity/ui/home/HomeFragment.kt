package com.example.glusity.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.glusity.databinding.FragmentHomeBinding
import com.example.glusity.ui.history.HistorystartActivity
import com.example.glusity.ui.pilihprediksi.PilihPrediksiActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.cardHealthPrediction.setOnClickListener {
            navigateToPredictionSelection()
        }

        binding.cardHistoryCheck.setOnClickListener {
            navigateToHistory()
        }

        return root
    }

    private fun navigateToPredictionSelection() {
        val intent = Intent(requireContext(), PilihPrediksiActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToHistory() {
        val intent = Intent(requireContext(), HistorystartActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}