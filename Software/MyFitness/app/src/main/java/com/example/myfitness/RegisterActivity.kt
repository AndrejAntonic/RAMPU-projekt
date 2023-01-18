package com.example.myfitness


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.myfitness.DataAccessObjects.UsersDAO
import com.example.myfitness.utils.Hash
import com.example.myfitness.utils.Validator
import kotlinx.coroutines.*
import java.security.MessageDigest


class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val btnRegister = findViewById<Button>(R.id.btn_register)

        val btnGoToLogin = findViewById<Button>(R.id.btn_go_to_login_from_register)
        btnGoToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btnRegister.setOnClickListener {

            val inputUsername = findViewById<EditText>(R.id.input_username)
            val inputEmail = findViewById<EditText>(R.id.input_email)
            val inputPassword = findViewById<EditText>(R.id.input_password)

            val providedUsername = inputUsername.text.toString()
            val providedEmail = inputEmail.text.toString()
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

            val that = this

            val scope = CoroutineScope(Dispatchers.Main)
            scope.launch {
                val usernameInUse = UsersDAO.GetUserByUsername(providedUsername)
                if (usernameInUse) {
                    withContext(Dispatchers.Main) {
                        inputUsername.setError("Korime se već koristi!")
                    }
                    return@launch
                }


                val emailInUse = UsersDAO.GetUserByEmail(providedEmail)
                if (emailInUse) {
                    withContext(Dispatchers.Main) {
                        inputEmail.setError("Email se već koristi!")
                    }
                    return@launch
                }

                UsersDAO.AddUser(providedUsername, providedEmail, Hash.hashPassword(providedPassword)).addOnSuccessListener {
                    val intent = Intent(that, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}