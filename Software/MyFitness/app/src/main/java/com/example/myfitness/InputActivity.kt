package com.example.myfitness

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.myfitness.DataAccessObjects.UsersDAO
import com.example.myfitness.utils.Validator
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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


            var errors = false

            if(!Validator.isActivityValid(providedActivity)){
                editActivity.setError("Aktivnost mora biti označena sa brojem 1-7 (dana u tjednu)!")
                errors = true
            }

            if(!Validator.isGenderValid(providedGender)){
                editGender.setError("Prihvatljivi unosi: 'Male', 'male', 'Female', 'female'")
                errors = true
            }

            if (errors) return@setOnClickListener

            val that = this

            val scope = CoroutineScope(Dispatchers.IO)
            scope.launch{
                UsersDAO.AddToUser(that, providedWeight.toDouble(), providedHeight.toDouble(), providedAge, providedActivity, providedGender)
                val intent = Intent(that, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}