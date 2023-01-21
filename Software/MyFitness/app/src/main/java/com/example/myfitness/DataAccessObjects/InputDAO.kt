package com.example.myfitness.DataAccessObjects


import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object InputDAO {
    fun AddInput(weight: String, height: String, age: String, activity: String, gender: String): Task<DocumentReference> {
        val db = Firebase.firestore
        val userMap = hashMapOf(
            "weight" to weight,
            "height" to height,
            "age" to age,
            "activityPerWeek" to activity,
            "gender" to gender
        )
        var result = false
        return db.collection("userInput").add(userMap)
    }
}