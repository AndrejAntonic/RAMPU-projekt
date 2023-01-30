package com.example.myfitness.DataAccessObjects

import android.R
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import com.example.myfitness.entities.Exercise


object ExerciseDAO {

    fun addExercise(exercise: Exercise, db: FirebaseFirestore) {
        val exercisesRef = db.collection("exercises")
        val data = hashMapOf(
            "name" to exercise.name,
            "description" to exercise.description,
            "difficulty" to exercise.difficulty,
            "equipment" to exercise.equipment,
            "bodyType" to exercise.bodyType,
        )
        exercisesRef.document(exercise.name).set(data)
            .addOnSuccessListener {
                Log.d("ExerciseDAO", "Vježba dodana!")
            }
            .addOnFailureListener {
                Log.e("ExerciseDAO", "Greška prilikom dodavanja vježbe!")
            }
    }

    suspend fun getAllExercises() : MutableList<Exercise> {
        val db = Firebase.firestore
        val exercisesList = mutableListOf<Exercise>()

        val exercises = db.collection("exercises")

        try {
            val result = exercises.get().await()
            for (document in result) {
                val exercise = document.toObject(Exercise::class.java)

                exercisesList.add(exercise)
            }
            return exercisesList
        } catch (e: Exception) {
            throw e
        }
    }




    suspend fun getAllExerciseNames() : MutableList<String> {
        val db = Firebase.firestore
        val exercisesList = mutableListOf<String>()

        val exercises = db.collection("exercises")

        try {
            val result = exercises.get().await()
            for (temp in result) {
                exercisesList.add(temp.id)
//                Log.d(TAG, "${temp.id} => ${temp.data}")
            }
            return exercisesList
        } catch (e: Exception) {
//            Log.w(TAG, "Error getting documents: ", exception)
            return mutableListOf() // returning an empty list in case the fetch fails
        }
    }

    private val BODY_PARTS = listOf("Leđa", "Prsa", "Noge", "Ramena", "Bicepsi", "Tricepsi")
}

