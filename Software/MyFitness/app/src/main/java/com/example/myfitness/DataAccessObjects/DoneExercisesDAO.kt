package com.example.myfitness.DataAccessObjects


import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import model.DoneExercise

object DoneExercisesDAO {
    fun add(doneExercise: DoneExercise) : Boolean {
        val db = Firebase.firestore
        val user = hashMapOf(
            "exerciseName" to doneExercise.exerciseName,
            "weight" to doneExercise.weight,
            "sets" to doneExercise.sets,
            "reps" to doneExercise.reps,
            "date" to doneExercise.date,
        )
        try {
            db.collection("doneExercises").document(doneExercise.username).collection("savedExercises").add(user)
            return true
        } catch (e: Exception) {
            return false
        }
    }
}