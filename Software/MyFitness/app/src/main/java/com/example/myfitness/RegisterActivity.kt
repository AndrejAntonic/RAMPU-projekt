package com.example.myfitness

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.myfitness.DataAccessObjects.UsersDAO
import com.example.myfitness.utils.Validator
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

//import com.google.firebase.auth.FirebaseAuth


class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val btnRegister = findViewById<Button>(R.id.btn_register)

        btnRegister.setOnClickListener {

            val inputUsername = findViewById<EditText>(R.id.input_username)
            val providedUsername = inputUsername.text.toString()
            val inputEmail = findViewById<EditText>(R.id.input_email)
            val providedEmail = inputEmail.text.toString()
            val inputPassword = findViewById<EditText>(R.id.input_password)
            val providedPassword = inputPassword.text.toString()

            // Validacija unosa
            var errors = false
            if (!Validator.isUsernameValid(providedUsername)) {
                inputUsername.setError("Korisničko ime mora biti između 3 i 20 znakova, bez specijalnih znakova")
                errors = true
            }

            if (!Validator.isEmailValid(providedEmail)) {
                inputEmail.setError("Niste unijeli ispravan email")
                errors = true
            }

            if (!Validator.isPasswordValid(providedPassword)) {
                inputPassword.setError("Lozinka mora biti duža od 6 znakova")
                errors = true
            }

            if (errors) return@setOnClickListener


            var success : Boolean = UsersDAO.AddUser(providedUsername, providedEmail, providedPassword)

            if (success) {
                val intent = Intent(this, InputActivity::class.java)
                startActivity(intent)
            } else {

            }



            // Todo spojit se na bazu


//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
        }
    }


}