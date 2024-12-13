package com.example.glusity.ui.obesitas

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.glusity.R
import com.example.glusity.data.UserPreference
import com.example.glusity.ui.hasilobesitas.HasilobesitasActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class ObesitasActivity : AppCompatActivity() {
  private var accessToken: String? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_obesitas)

    accessToken = UserPreference.getAccessToken(this)

    val etVegetable = findViewById<EditText>(R.id.et_vegetable)
    val etMeals = findViewById<EditText>(R.id.et_meals)
    val etWater = findViewById<EditText>(R.id.et_water)
    val etExercise = findViewById<EditText>(R.id.et_exercise)
    val etGadget = findViewById<EditText>(R.id.et_gadget)
    val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
    val btnSubmit = findViewById<Button>(R.id.btn_submit)

    val gender = intent.getIntExtra("gender", 0)
    val age = intent.getIntExtra("age", 0)
    val height = intent.getDoubleExtra("height", 0.0)
    val weight = intent.getDoubleExtra("weight", 0.0)

    btnSubmit.setOnClickListener {
      val vegetable = etVegetable.text.toString().trim()
      val meals = etMeals.text.toString().trim()
      val water = etWater.text.toString().trim()
      val exercise = etExercise.text.toString().trim()
      val gadget = etGadget.text.toString().trim()

      if (vegetable.isEmpty() || meals.isEmpty() || water.isEmpty() ||
        exercise.isEmpty() || gadget.isEmpty()
      ) {
        Toast.makeText(this, "Mohon lengkapi semua data!", Toast.LENGTH_SHORT).show()
        return@setOnClickListener
      }

      val requestData = JSONObject().apply {
        put("gender", gender)
        put("age", age)
        put("height", height)
        put("weight", weight)
        put("fcvc", vegetable.toDouble())
        put("ncp", meals.toDouble())
        put("ch2o", water.toDouble())
        put("faf", exercise.toDouble())
        put("tue", gadget.toDouble())
      }

      sendObesityPredictionRequest(requestData, progressBar)
    }
  }

  private fun sendObesityPredictionRequest(requestData: JSONObject, progressBar: ProgressBar) {
    lifecycleScope.launch {
      progressBar.visibility = ProgressBar.VISIBLE
      val url = "https://backend-model-api-562817970631.asia-southeast2.run.app/predict/obesity"
      val token = accessToken
      if (token.isNullOrEmpty()) {
        Toast.makeText(this@ObesitasActivity, "Token tidak ditemukan. Silakan login ulang.", Toast.LENGTH_SHORT).show()
        return@launch
      }

      val requestBody = requestData.toString()
        .toRequestBody("application/json".toMediaTypeOrNull())

      val request = Request.Builder()
        .url(url)
        .addHeader("Authorization", "Bearer $token")
        .post(requestBody)
        .build()

      val client = OkHttpClient()

      try {
        val response = withContext(Dispatchers.IO) {
          client.newCall(request).execute()
        }

        if (response.isSuccessful) {
          val responseBody = response.body?.string()
          navigateToHasilObesitasActivity(responseBody)
        } else {
          throw Exception("HTTP ${response.code}: ${response.message}")
        }
      } catch (e: Exception) {
        println("Error: ${e.message}")
        Toast.makeText(this@ObesitasActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
      } finally {
        progressBar.visibility = ProgressBar.GONE
      }
    }
  }

  private fun navigateToHasilObesitasActivity(responseBody: String?) {
    val intent = Intent(this@ObesitasActivity, HasilobesitasActivity::class.java).apply {
      putExtra("prediction_result", responseBody)
    }
    startActivity(intent)
  }
}