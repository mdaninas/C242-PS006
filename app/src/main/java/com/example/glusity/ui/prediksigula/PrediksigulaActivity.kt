package com.example.glusity.ui.prediksigula

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.glusity.R
import com.example.glusity.data.UserPreference
import com.example.glusity.ui.hasilgula.HasilgulaActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class PrediksigulaActivity : AppCompatActivity() {
  private var accessToken: String? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_prediksigulaa)

    accessToken = UserPreference.getAccessToken(this)

    val etGlucose = findViewById<EditText>(R.id.et_glucose)
    val etHbA1c = findViewById<EditText>(R.id.et_hba1c)
    val etHypertension = findViewById<Spinner>(R.id.et_hypertension)
    val etSmoking = findViewById<Spinner>(R.id.et_smoking)
    val etHeartDisease = findViewById<Spinner>(R.id.et_heart_disease)
    val cbUnknown = findViewById<CheckBox>(R.id.cb_unknown)
    val btnSubmit = findViewById<Button>(R.id.btn_send)
    val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
    val yesNoAdapter = ArrayAdapter.createFromResource(
      this, R.array.yes_no_options, android.R.layout.simple_spinner_item
    )
    yesNoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    etHeartDisease.adapter = yesNoAdapter
    etHypertension.adapter = yesNoAdapter
    etSmoking.adapter = yesNoAdapter

    cbUnknown.setOnCheckedChangeListener { _, isChecked ->
      etHbA1c.isEnabled = !isChecked
      if (isChecked) etHbA1c.text.clear()
    }

    val gender = intent.getIntExtra("gender", 0)
    val age = intent.getIntExtra("age", 0)
    val height = intent.getDoubleExtra("height", 0.0)
    val weight = intent.getDoubleExtra("weight", 0.0)

    btnSubmit.setOnClickListener {
      val glucose = etGlucose.text.toString().trim()
      val hbA1c = etHbA1c.text.toString().trim()
      val hypertension = etHypertension.selectedItem.toString()
      val smoking = etSmoking.selectedItem.toString()
      val heartDisease = etHeartDisease.selectedItem.toString()
      val isUnknownChecked = cbUnknown.isChecked

      if (glucose.isEmpty()) {
        Toast.makeText(this, "Mohon isi nilai Glukosa Darah!", Toast.LENGTH_SHORT).show()
        return@setOnClickListener
      }

      val requestData = JSONObject().apply {
        put("blood_glucose_level", glucose.toIntOrNull() ?: 0)
        put("has_hba1c", if (isUnknownChecked) 0 else 1)
        if (!isUnknownChecked) {
          val hbA1cValue = hbA1c.toDoubleOrNull()
          if (hbA1cValue != null) {
            put("hba1c", hbA1cValue)
          } else {
            Toast.makeText(this@PrediksigulaActivity, "HbA1c harus berupa angka!", Toast.LENGTH_SHORT).show()
            return@setOnClickListener
          }
        }
        put("gender", gender)
        put("age", age)
        put("height", height)
        put("weight", weight)
        put("hypertension", if (hypertension.equals("Yes", ignoreCase = true)) 1 else 0)
        put("smoking_history", if (smoking.equals("Yes", ignoreCase = true)) 1 else 0)
        put("heart_disease", if (heartDisease.equals("Yes", ignoreCase = true)) 1 else 0)
      }

      sendGulaPredictionRequest(requestData, progressBar)
    }
  }

  private fun sendGulaPredictionRequest(requestData: JSONObject, progressBar: ProgressBar) {
    lifecycleScope.launch {
      progressBar.visibility = ProgressBar.VISIBLE
      val url = "https://backend-model-api-562817970631.asia-southeast2.run.app/predict/diabetes"
      val token = accessToken
      if (token.isNullOrEmpty()) {
        Toast.makeText(this@PrediksigulaActivity, "Token tidak ditemukan. Silakan login ulang.", Toast.LENGTH_SHORT).show()
        progressBar.visibility = ProgressBar.GONE
        return@launch
      }

      try {
        val sanitizedData = JSONObject().apply {
          put("blood_glucose_level", requestData.optInt("blood_glucose_level", 0))
          put("has_hba1c", requestData.optInt("has_hba1c", 0))
          if (requestData.optInt("has_hba1c") == 1) {
            put("hba1c", requestData.optDouble("hba1c", 0.0))
          }
          put("gender", requestData.optInt("gender", 0))
          put("age", requestData.optInt("age", 0))
          put("height", requestData.optDouble("height", 0.0))
          put("weight", requestData.optDouble("weight", 0.0))
          put("hypertension", requestData.optInt("hypertension", 0))
          put("smoking_history", requestData.optInt("smoking_history", 0))
          put("heart_disease", requestData.optInt("heart_disease", 0))
        }

        val requestBody = sanitizedData.toString().toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
          .url(url)
          .addHeader("Authorization", "Bearer $token")
          .post(requestBody)
          .build()

        val client = OkHttpClient()

        val response = withContext(Dispatchers.IO) {
          client.newCall(request).execute()
        }

        if (response.isSuccessful) {
          val responseBody = response.body?.string()

          navigateToHasilGulaActivity(responseBody)
        } else {
          throw Exception("HTTP ${response.code}: ${response.message}")
        }
      } catch (e: Exception) {
        Toast.makeText(this@PrediksigulaActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
      } finally {
        progressBar.visibility = ProgressBar.GONE
      }
    }
  }

  private fun navigateToHasilGulaActivity(responseBody: String?) {
    val intent = Intent(this@PrediksigulaActivity, HasilgulaActivity::class.java).apply {
      putExtra("prediction_result", responseBody)
    }
    startActivity(intent)
  }
}