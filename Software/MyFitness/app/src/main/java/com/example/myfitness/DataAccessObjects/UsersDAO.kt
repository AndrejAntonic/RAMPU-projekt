package com.example.myfitness.DataAccessObjects


import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object UsersDAO {

     fun AddUser(username: String, email: String, password: String) : Task<DocumentReference> {
         val db = Firebase.firestore
         val user = hashMapOf(
             "username" to username,
             "email" to email,
             "password" to password
         )
         var result = false
         return db.collection("users").add(user)
    }

    fun Edituser(username: String, email: String, password: String, Height: Double, Weight: Double, Age: Int, Activity: Int, gender: String, metabolicRate: Double) {

    }

    suspend fun GetUserByUsername(username: String) : Boolean {
        val db = Firebase.firestore
        val user = db.collection("users")
            .whereEqualTo("username", username)
            .get().await()

        return user.size() > 0
    }

    suspend fun GetUserByEmail(email: String) : Boolean {
        val db = Firebase.firestore
        val user = db.collection("users")
            .whereEqualTo("email", email)
            .get().await()

        return user.size() > 0
    }

    suspend fun LoginUser(username: String, password: String) : Boolean {
        val db = Firebase.firestore
        val user = db.collection("users")
            .whereEqualTo("username", username)
            .whereEqualTo("password", password)
            .get().await()

        return user.size() > 0
    }

    fun getCurrentUser(context : Context) : String {
        val prefs = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        return prefs.getString("username", "")!!
    }
}