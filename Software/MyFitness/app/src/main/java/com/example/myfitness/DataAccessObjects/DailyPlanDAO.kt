package com.example.myfitness.DataAccessObjects

import com.example.myfitness.entities.DoneExercise
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object DailyPlanDAO {
    fun add(doneExercise: DoneExercise, username: String) : Boolean {
        val db = Firebase.firestore
        val exercise = hashMapOf(
            "exerciseName" to doneExercise.exerciseName,
            "weight" to doneExercise.weight,
            "sets" to doneExercise.sets,
            "reps" to doneExercise.reps,
            "date" to doneExercise.date,
        )
        try {
            db.collection("dailyPlan").document(username).collection("savedDailyPlan").add(exercise)
            return true
        } catch (e: Exception) {
            return false
        }
    }
}