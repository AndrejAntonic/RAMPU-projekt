package com.example.myfitness.DataAccessObjects


import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import com.google.firebase.auth.ktx.auth


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

    suspend fun EditUser(username: String, email: String, password: String, weight: Double) {
        val db = Firebase.firestore
        val userRef = db.collection("users").whereEqualTo("username", username)
        val document = userRef.get().await()
        if (document.size() > 0) {
            val user = document.documents[0].reference
            user.update("username", username).await()
            user.update("email", email).await()
            user.update("password", password).await()
            user.update("weight", weight).await()
        } else {
            Log.d("EditUser", "Nije moguce promijeniti podatke")
        }
    }

}
