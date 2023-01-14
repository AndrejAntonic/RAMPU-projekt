package com.example.myfitness.DataAccessObjects

import android.content.ContentValues
import android.content.Intent
import android.util.Log
import com.example.myfitness.InputActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object UsersDAO {
    fun AddUser(username: String, email: String, password: String) : Boolean {
        val db = Firebase.firestore

        val user = hashMapOf(
            "username" to username,
            "email" to email,
            "password" to password,
        )

        var success : Boolean = false;
        // Add a new document with a generated ID
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                success = true
            }
            .addOnFailureListener { e ->
                success = false
            }
        return success
    }

    fun Edituser(username: String, email: String, password: String, Height: Double, Weight: Double, Age: Int, Activity: Int, gender: String, metabolicRate: Double) {

    }
}