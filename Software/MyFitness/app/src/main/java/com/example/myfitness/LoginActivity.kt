package com.example.myfitness

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnLogin = findViewById<Button>(R.id.btn_login);

        btnLogin.setOnClickListener {
            val insertedUsername = findViewById<EditText>(R.id.input_username_login).text.toString()
            val insertedPassword = findViewById<EditText>(R.id.input_password_login).text.toString()
            val intent = Intent(this, MainActivity::class.java)
            //TODO implementirati logiku provjeravanja podataka iz baze
            if(insertedUsername == "test" && insertedPassword == "test")
                startActivity(intent)
            else
                Toast.makeText(this@LoginActivity, "Uneseni podaci nisu valjani", Toast.LENGTH_SHORT).show()
        }
    }
}