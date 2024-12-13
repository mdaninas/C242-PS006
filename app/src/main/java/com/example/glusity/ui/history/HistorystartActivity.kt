package com.example.glusity.ui.history

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.glusity.R
import com.example.glusity.data.HistoryAdapter
import com.example.glusity.data.HistoryItem
import com.example.glusity.data.UserPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL

class HistorystartActivity : AppCompatActivity() {
  private var accessToken: String? = null
  private lateinit var listView: ListView
  private lateinit var historyAdapter: HistoryAdapter
  private lateinit var centerImage: ImageView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_historystart)

    accessToken = UserPreference.getAccessToken(this)

    val backButton = findViewById<ImageView>(R.id.backbutton)
    backButton.setOnClickListener {
      finish()
    }

    listView = findViewById(R.id.results_list_view)
    centerImage = findViewById(R.id.center_image)
    historyAdapter = HistoryAdapter(this, ArrayList())
    listView.adapter = historyAdapter

    fetchData()
  }

  private fun fetchData() {
    val token = accessToken
    lifecycleScope.launch {
      try {
        val url = "https://backend-model-api-562817970631.asia-southeast2.run.app/predict/history"

        if (token.isNullOrEmpty()) {
          Log.e("fetchData", "Token kosong atau null.")
          Toast.makeText(
            this@HistorystartActivity,
            "Token tidak ditemukan. Silakan login ulang.",
            Toast.LENGTH_SHORT
          ).show()
          return@launch
        }

        val response = withContext(Dispatchers.IO) {
          val connection = URL(url).openConnection() as HttpURLConnection
          connection.apply {
            requestMethod = "GET"
            setRequestProperty("Authorization", "Bearer $token")
            connectTimeout = 10000
            readTimeout = 10000
          }

          try {
            val responseCode = connection.responseCode
            val result = if (responseCode == HttpURLConnection.HTTP_OK) {
              connection.inputStream.bufferedReader().use { it.readText() }
            } else {
              connection.errorStream?.bufferedReader()?.use { it.readText() } ?: "Unknown error"
            }
            Pair(responseCode, result)
          } finally {
            connection.disconnect()
          }
        }

        val (responseCode, result) = response
        if (responseCode == HttpURLConnection.HTTP_OK) {
          val historyData = parseHistoryData(result)
          updateUI(historyData)
        } else {
          Log.e("fetchData", "Error get $responseCode: $result")
        }

      } catch (e: SocketTimeoutException) {
        Log.e("fetchData", "Timeout: Server terlalu lama merespons.", e)
      } catch (e: Exception) {
        Log.e("fetchData", "Exception: ${e.message}", e)
      }
    }
  }

  private fun parseHistoryData(jsonResponse: String): List<HistoryItem> {
    val historyList = mutableListOf<HistoryItem>()
    try {
      val jsonObject = JSONObject(jsonResponse)
      val historyObject = jsonObject.getJSONObject("history")
      val diabetesArray = historyObject.getJSONArray("diabetes_predictions")
      for (i in 0 until diabetesArray.length()) {
        val item = diabetesArray.getJSONObject(i)
        val age = item.getDouble("age")
        val prediction = item.getDouble("prediction")
        historyList.add(
          HistoryItem(
            title = "Diabetes - Age: $age",
            description = "Predictions: $prediction"
          )
        )
      }

      val obesityArray = historyObject.getJSONArray("obesity_predictions")
      for (i in 0 until obesityArray.length()) {
        val item = obesityArray.getJSONObject(i)
        val age = item.getInt("age")
        val predictedCategory = item.getString("predicted_category")
        historyList.add(
          HistoryItem(
            title = "Obesity - Age: $age",
            description = "Predicted Category: $predictedCategory"
          )
        )
      }
    } catch (e: JSONException) {
      Log.e("parseHistoryData", "JSON parsing error: ${e.message}", e)
    }
    return historyList
  }

  private fun updateUI(historyList: List<HistoryItem>) {
    if (historyList.isNotEmpty()) {
      historyAdapter.clear()
      historyAdapter.addAll(historyList)
      listView.visibility = View.VISIBLE
      centerImage.visibility = View.GONE
    } else {
      listView.visibility = View.GONE
      centerImage.visibility = View.VISIBLE
    }
  }
}