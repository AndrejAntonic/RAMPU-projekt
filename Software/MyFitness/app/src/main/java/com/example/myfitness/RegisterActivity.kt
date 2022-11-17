package com.example.myfitness

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val btnRegister = findViewById<Button>(R.id.btn_register)

        btnRegister.setOnClickListener {

            val inputUsername = findViewById<EditText>(R.id.input_username).text.toString()
            val inputEmail = findViewById<EditText>(R.id.input_email).text.toString()
            val inputPassword = findViewById<EditText>(R.id.input_password).text.toString()

            // Todo napravit validaciju unosa
            // Todo spojit se na bazu


            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}