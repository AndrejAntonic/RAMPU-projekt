package com.example.myfitness

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.myfitness.DataAccessObjects.InputDAO
import com.example.myfitness.DataAccessObjects.UsersDAO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

class InputActivity : AppCompatActivity() {


    private var db = Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)

        val btnSubmit = findViewById<Button>(R.id.btn_Submit)

        btnSubmit.setOnClickListener{

            val editWeight = findViewById<EditText>(R.id.edit_weight)
            val editHeight = findViewById<EditText>(R.id.edit_height)
            val editAge = findViewById<EditText>(R.id.edit_age)
            val editActivity = findViewById<EditText>(R.id.edit_activity)
            val editGender = findViewById<EditText>(R.id.edit_gender)

            val providedWeight = editWeight.text.toString()
            val providedHeight = editHeight.text.toString()
            val providedAge = editAge.text.toString()
            val providedActivity = editActivity.text.toString()
            val providedGender = editGender.text.toString()

            val that = this

            InputDAO.AddInput(providedWeight, providedHeight, providedAge, providedActivity, providedGender).addOnSuccessListener {
                val prefs = getSharedPreferences("userInput", Context.MODE_PRIVATE)
                val editor = prefs.edit()
                editor.putString("weight", editWeight.toString())
                editor.apply()

                val intent = Intent(that, MainActivity::class.java)
                startActivity(intent)
            }

        }
    }
}