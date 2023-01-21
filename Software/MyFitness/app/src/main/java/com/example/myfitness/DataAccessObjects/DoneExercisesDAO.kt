package com.example.myfitness.DataAccessObjects


import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import model.DoneExercise
import model.Exercise

object DoneExercisesDAO {
    fun add(doneExercise: DoneExercise, username: String) : Boolean {
        val db = Firebase.firestore
        val exerciseMap = hashMapOf(
            "weight" to doneExercise.weight,
            "sets" to doneExercise.sets,
            "reps" to doneExercise.reps,
            "date" to doneExercise.date,
        )
        try {
            db.collection("doneExercises").document(username).collection(doneExercise.exerciseName).add(exerciseMap)
            return true
        } catch (e: Exception) {
            return false
        }
    }

    suspend fun getAllDoneExercisesForUser(username : String) : MutableList<DoneExercise> {
        val db = Firebase.firestore
        val exercisesList = mutableListOf<DoneExercise>()
        val exercises = db.collection("doneExercises").document(username).collection("savedExercises")

        try {
            val result = exercises.get().await()
            for (document in result) {
                val exercise = document.toObject(DoneExercise::class.java)
                println(exercise)
                exercisesList.add(exercise)
            }
            return exercisesList
        } catch (e: Exception) {
            println(e)
            return mutableListOf()
        }
    }
}