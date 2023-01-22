package com.example.myfitness.DataAccessObjects


import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import model.CreatedWorkout
import model.DoneExercise

object CreatedWorkoutDAO {
    fun add(CreatedWorkout: CreatedWorkout, username: String) : Boolean {
        val db = Firebase.firestore
        val exercise = hashMapOf(
            "exerciseName" to CreatedWorkout.exerciseName,
            "date" to CreatedWorkout.date,
        )
        try {
            db.collection("CreatedWorkout").document(username).collection("CreatedWorkouts").add(exercise)
            return true
        } catch (e: Exception) {
            return false
        }
    }

    /*suspend fun getAllDoneExercisesForUser(username : String) : MutableList<DoneExercise> {
        val db = Firebase.firestore
        val exercisesList = mutableListOf<DoneExercise>()

        val exercises = db.collection("doneExercises").document(username).collection("savedExercises")

        try {
            val result = exercises.get().await()
            for (document in result) {
                val exercise = document.toObject(DoneExercise::class.java)
                exercisesList.add(exercise)
            }
            return exercisesList
        } catch (e: Exception) {
            return mutableListOf()
        }
    }*/
}