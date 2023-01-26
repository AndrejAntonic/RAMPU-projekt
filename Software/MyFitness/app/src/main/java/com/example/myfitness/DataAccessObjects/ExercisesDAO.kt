package com.example.myfitness.DataAccessObjects

import android.content.ContentValues.TAG
import android.util.Log
import com.example.myfitness.entities.DoneExercise
import com.example.myfitness.entities.Exercises
import com.example.myfitness.entities.Plan
import com.example.myfitness.utils.Hash
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import kotlinx.datetime.LocalDateTime
import java.util.Calendar
import java.util.logging.SimpleFormatter
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

    fun addPlan(plan: List<Plan>, username: String) : Boolean {
        val db = Firebase.firestore
        for(day in plan) {
            var map: HashMap<String, Any> = HashMap()
            map["timeStamp"] = FieldValue.serverTimestamp()
            var idk = 1
            for(exercises in day.exercise) {
                map["vjezba$idk"] = exercises.exercise
                idk++
            }
            db.collection("workoutPlan").document(username).collection(day.day).add(map)
        }

        return true
    }
}