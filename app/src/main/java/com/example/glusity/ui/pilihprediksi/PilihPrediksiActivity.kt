package com.example.glusity.ui.pilihprediksi

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.glusity.R
import com.example.glusity.ui.obesitas.ObesitasActivity
import com.example.glusity.ui.prediksigula.PrediksigulaActivity
import com.google.android.material.textfield.TextInputEditText

class PilihPrediksiActivity : AppCompatActivity() {

  private lateinit var btnSubmit: Button
  private lateinit var cardGulaDarah: LinearLayout
  private lateinit var cardObesitas: LinearLayout
  private lateinit var inputAge: TextInputEditText
  private lateinit var inputWeight: TextInputEditText
  private lateinit var inputHeight: TextInputEditText
  private lateinit var genderRadioGroup: RadioGroup

  private var selectedActivity: String? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_pilih_prediksi)

    btnSubmit = findViewById(R.id.btnSubmit)
    cardGulaDarah = findViewById(R.id.cardGulaDarah)
    cardObesitas = findViewById(R.id.cardObesitas)
    inputAge = findViewById(R.id.inputAge)
    inputWeight = findViewById(R.id.inputWeight)
    inputHeight = findViewById(R.id.inputHeight)
    genderRadioGroup = findViewById(R.id.genderRadioGroup)

    cardGulaDarah.setOnClickListener {
      selectedActivity = "Gula Darah"
      cardGulaDarah.setBackgroundResource(R.drawable.card_background_pink)
      cardObesitas.setBackgroundResource(R.drawable.card_background_blue)
      Toast.makeText(this, "Navigasi ke PrediksiGulaActivity", Toast.LENGTH_SHORT).show()
    }

    cardObesitas.setOnClickListener {
      selectedActivity = "Obesitas"
      cardObesitas.setBackgroundResource(R.drawable.card_background_pink)
      cardGulaDarah.setBackgroundResource(R.drawable.card_background_blue)
      Toast.makeText(this, "Navigasi ke PrediksiObesitasActivity", Toast.LENGTH_SHORT).show()
    }

    btnSubmit.setOnClickListener {
      val age = inputAge.text.toString().toIntOrNull()
      val weight = inputWeight.text.toString().toDoubleOrNull()
      val height = inputHeight.text.toString().toDoubleOrNull()

      val selectedGenderId = genderRadioGroup.checkedRadioButtonId
      val gender = if (selectedGenderId == R.id.radioMale) 1 else if (selectedGenderId == R.id.radioFemale) 0 else null

      if (age == null || weight == null || height == null || gender == null) {
        Toast.makeText(this, "Harap isi semua data dengan benar", Toast.LENGTH_SHORT).show()
        return@setOnClickListener
      }

      when (selectedActivity) {
        "Gula Darah" -> {
          val intent = Intent(this, PrediksigulaActivity::class.java).apply {
            putExtra("gender", gender)
            putExtra("age", age)
            putExtra("height", height)
            putExtra("weight", weight)
          }
          startActivity(intent)
        }
        "Obesitas" -> {
          val intent = Intent(this, ObesitasActivity::class.java).apply {
            putExtra("gender", gender)
            putExtra("age", age)
            putExtra("height", height)
            putExtra("weight", weight)
          }
          startActivity(intent)
        }
        else -> {
          Toast.makeText(this, "Silahkan pilih salah satu card terlebih dahulu", Toast.LENGTH_SHORT).show()
        }
      }
    }
  }
}