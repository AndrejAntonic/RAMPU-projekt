package com.example.myfitness

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.myfitness.DataAccessObjects.UsersDAO
import com.example.myfitness.helpers.DateHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)


        val btnGoToRegister = findViewById<Button>(R.id.btn_go_to_register)
        val btnGoToLogin = findViewById<Button>(R.id.btn_go_to_login)

        btnGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        btnGoToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val that = this
        val lastUsername = UsersDAO.getCurrentUser(this)
        val scope = CoroutineScope(Dispatchers.IO)

        scope.launch {
            val sessionTimestamp = UsersDAO.getSession(lastUsername)
            println("TIMESTAMP " + sessionTimestamp)
            val sessionExpired = DateHelper.hasTimePassed(sessionTimestamp)
            println("EXPIRED " + sessionExpired)

            if (!sessionExpired) {
                withContext(Dispatchers.Main) {
                    val intent = Intent(that, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}