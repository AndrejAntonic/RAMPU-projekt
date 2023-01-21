package com.example.myfitness

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.math.BigDecimal
import java.math.RoundingMode

class InputActivity : AppCompatActivity() {

    lateinit var editWeight : EditText
    lateinit var editHeight : EditText
    lateinit var editAge : EditText
    lateinit var editActivity : EditText
    lateinit var editGender : EditText
    lateinit var btnSubmit : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)

        editWeight = findViewById(R.id.edit_weight)
        editHeight = findViewById(R.id.edit_height)
        editAge = findViewById(R.id.edit_age)
        editActivity = findViewById(R.id.edit_activity)
        editGender = findViewById(R.id.edit_gender)
        btnSubmit = findViewById(R.id.btn_Submit)
    }
}