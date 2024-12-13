package com.example.glusity.ui.hasilgula

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ImageButton
import android.widget.TextView
import com.example.glusity.R
import org.json.JSONObject

class HasilgulaActivity : AppCompatActivity() {

  private fun parseAndDisplayData(jsonResponse: String) {
    try {
      val jsonObject = JSONObject(jsonResponse)
      val prediction = jsonObject.optString("prediction", "Tidak ada prediksi")
      val recommendation = jsonObject.optString("recommendation", "Tidak ada rekomendasi").replace("*", "")
      val riskLevel = jsonObject.optString("risk_level", "Tidak ada tingkat risiko")  // Ambil risk level
      val tvPredictionValue = findViewById<TextView>(R.id.tv_prediction_value)
      val tvSuggestionsContent = findViewById<TextView>(R.id.tv_suggestions_content)
      val tvRiskLevel = findViewById<TextView>(R.id.tv_risk_level)

      tvPredictionValue.text = "Prediksi: $prediction"
      tvSuggestionsContent.text = recommendation
      tvRiskLevel.text = "Tingkat Risiko: $riskLevel"

    } catch (e: Exception) {
      e.printStackTrace()
      showError()
    }
  }

  private fun showError() {
    val tvPredictionValue = findViewById<TextView>(R.id.tv_prediction_value)
    val tvSuggestionsContent = findViewById<TextView>(R.id.tv_suggestions_content)
    val tvRiskLevel = findViewById<TextView>(R.id.tv_risk_level)

    tvPredictionValue.text = "Error"
    tvSuggestionsContent.text = "Terjadi kesalahan saat memuat data. Mohon coba lagi."
    tvRiskLevel.text = "Tidak ada data tingkat risiko."
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContentView(R.layout.activity_hasilgula)

    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
      insets
    }

    val predictionResult = intent.getStringExtra("prediction_result")
    if (predictionResult != null) {
      parseAndDisplayData(predictionResult)
    } else {
      showError()
    }

    findViewById<ImageButton>(R.id.btn_back).setOnClickListener {
      onBackPressed()
    }
  }
}