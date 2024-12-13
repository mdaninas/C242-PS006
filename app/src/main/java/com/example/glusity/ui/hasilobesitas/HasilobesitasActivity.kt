package com.example.glusity.ui.hasilobesitas

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.glusity.R
import org.json.JSONObject

class HasilobesitasActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContentView(R.layout.activity_hasilobesitas)

    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
      insets
    }

    val btnBack = findViewById<ImageButton>(R.id.btn_back)
    btnBack.setOnClickListener {
      onBackPressed()
    }

    val predictionResult = intent.getStringExtra("prediction_result")
    if (predictionResult != null) {
      parseAndDisplayData(predictionResult)
    } else {
      showError()
    }
  }

  private fun parseAndDisplayData(jsonResponse: String) {
    try {
      val jsonObject = JSONObject(jsonResponse)
      val category = jsonObject.getString("category")
      val recommendation = jsonObject.getString("recommendation").replace("*", "")
      // Populate the views
      val tvPredictionStatus = findViewById<TextView>(R.id.tv_prediction_status)
      val tvSuggestionsTitle = findViewById<TextView>(R.id.tv_suggestions_title)
      val tvSuggestionsContent = findViewById<TextView>(R.id.tv_suggestions_content)

      tvPredictionStatus.text = category // Set category to prediction status
      tvSuggestionsTitle.text = "Saran" // Static title, can be customized
      tvSuggestionsContent.text = recommendation // Set recommendation content
    } catch (e: Exception) {
      e.printStackTrace()
      showError()
    }
  }

  private fun showError() {
    val tvPredictionStatus = findViewById<TextView>(R.id.tv_prediction_status)
    val tvSuggestionsContent = findViewById<TextView>(R.id.tv_suggestions_content)

    tvPredictionStatus.text = "Error"
    tvSuggestionsContent.text = "Terjadi kesalahan saat memuat data. Mohon coba lagi."
  }
}