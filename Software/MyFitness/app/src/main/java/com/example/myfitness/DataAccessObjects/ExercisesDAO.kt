package com.example.myfitness.DataAccessObjects

import android.content.ContentValues.TAG
import android.util.Log
import com.example.myfitness.entities.Exercises
import com.example.myfitness.entities.Plan
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object ExercisesDAO {
    suspend fun getExercise(bodyPart: String, difficulty: Int): MutableList<Exercises> {
        val db = Firebase.firestore
        val exercisesList = mutableListOf<Exercises>()

        val exercise = db.collection("exercises")
        val query = exercise.whereEqualTo("bodyType", bodyPart)

        try {
            val result = query.get().await()
            for(temp in result) {
                exercisesList.add(Exercises(temp.id))
                Log.d(TAG, "${temp.id} => ${temp.data}")
            }

            return exercisesList
        } catch (e: Exception) {
            Log.w(TAG, "Error getting documents: ", e)

            return exercisesList
        }
    }
}