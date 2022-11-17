package com.example.myfitness

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.myfitness.R

class InputActivity : AppCompatActivity() {

    lateinit var display : TextView
    lateinit var editWeight : EditText
    lateinit var editHeight : EditText
    lateinit var editAge : EditText
    lateinit var genderMale : Button
    lateinit var genderFemale : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)

        display = findViewById(R.id.display_result)
        editWeight = findViewById(R.id.edit_weight)
        editHeight = findViewById(R.id.edit_height)
        editAge = findViewById(R.id.edit_age)
        genderMale = findViewById(R.id.male)
        genderFemale = findViewById(R.id.female)
    }
}