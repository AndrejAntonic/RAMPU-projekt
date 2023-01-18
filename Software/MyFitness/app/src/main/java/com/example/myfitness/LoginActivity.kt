package com.example.myfitness

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.myfitness.DataAccessObjects.UsersDAO
import com.example.myfitness.utils.Hash
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnGoToRegister = findViewById<Button>(R.id.btn_go_to_register_from_login)

        btnGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val btnLogin = findViewById<Button>(R.id.btn_login);

        btnLogin.setOnClickListener {
            val inputUsername = findViewById<EditText>(R.id.input_username_login)
            val inputPassword = findViewById<EditText>(R.id.input_password_login)

            val insertedUsername = inputUsername.text.toString()
            val insertedPassword = inputPassword.text.toString()
            val hashedPassword = Hash.hashPassword(insertedPassword)
            val intent = Intent(this, MainActivity::class.java)

            val that = this

            val scope = CoroutineScope(Dispatchers.Main)
            scope.launch {
                val usernameExists = UsersDAO.GetUserByUsername(insertedUsername)
                if (!usernameExists) {
                    withContext(Dispatchers.Main) {
                        inputUsername.setError("Korime ne postoji!")
                    }
                    return@launch
                }

                val successfulLogin = UsersDAO.LoginUser(insertedUsername, hashedPassword)
                if(!successfulLogin) {
                    withContext(Dispatchers.Main) {
                        inputPassword.setError("Pogrešna lozinka")
                    }
                    return@launch
                } else {
                    val intent = Intent(that, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}