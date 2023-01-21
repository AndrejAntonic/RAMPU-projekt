package com.example.myfitness

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.math.BigDecimal
import java.math.RoundingMode

class CalculatorActivity : AppCompatActivity() {

    lateinit var display : TextView
    lateinit var editWeight : EditText
    lateinit var editHeight : EditText
    lateinit var editAge : EditText
    lateinit var genderMale : Button
    lateinit var genderFemale : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        display = findViewById(R.id.display_result1)
        editWeight = findViewById(R.id.edit_weight1)
        editHeight = findViewById(R.id.edit_height1)
        editAge = findViewById(R.id.edit_age1)
        genderMale = findViewById(R.id.male1)
        genderFemale = findViewById(R.id.female1)

        genderMale.setOnClickListener{
            val firstEdit = editWeight.text.toString().toInt()
            val secondEdit = editHeight.text.toString().toInt()
            val thirdEdit = editAge.text.toString().toInt()
            Male(firstEdit, secondEdit, thirdEdit)
        }

        genderFemale.setOnClickListener {
            val firstEdit = editWeight.text.toString().toInt()
            val secondEdit = editHeight.text.toString().toInt()
            val thirdEdit = editAge.text.toString().toInt()
            Female(firstEdit, secondEdit, thirdEdit)
        }
    }

    private fun Male(firstEdit: Int, secondEdit: Int, thirdEdit: Int)
    {
        val result = 88.362 + (13.397 * firstEdit) + (4.799 * secondEdit) - (5.677 * thirdEdit)
        val decimal = BigDecimal(result).setScale(3, RoundingMode.HALF_EVEN)
        display.text = decimal.toString()
    }

    private fun Female(firstEdit: Int, secondEdit: Int, thirdEdit: Int)
    {
        val result = 447.593 + (9.247 * firstEdit) + (3.098 * secondEdit) - (4.330 * thirdEdit)
        val decimal = BigDecimal(result).setScale(3, RoundingMode.HALF_EVEN)
        display.text = decimal.toString()
    }
}