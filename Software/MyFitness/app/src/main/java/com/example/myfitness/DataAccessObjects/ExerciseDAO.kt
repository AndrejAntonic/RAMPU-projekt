package com.example.myfitness.DataAccessObjects

import android.R

import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import model.Exercise


object ExerciseDAO {

    fun addExercise(exercise: Exercise, db: FirebaseFirestore) {
        val exercisesRef = db.collection("exercises")
        exercisesRef.add(exercise)
            .addOnSuccessListener {
                Log.d("ExerciseDAO", "Vježba dodana!")
            }
            .addOnFailureListener {
                Log.e("ExerciseDAO", "Greška prilikom dodavanja vježbe!")
            }
    }

    fun fillSpinner(spinner: Spinner) {
        spinner.adapter = ArrayAdapter(
            spinner.context,
            R.layout.simple_spinner_item,
            BODY_PARTS
        )
    }

    fun getExercisesByBodyPard(bodyPart: String, db: FirebaseFirestore, callback: (List<Exercise>) -> Unit) {
        val exercisesRef = db.collection("exercises")
        exercisesRef
            .whereEqualTo("bodyPart", bodyPart)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val exercises = querySnapshot.toObjects(Exercise::class.java)
                callback(exercises)
            }
            .addOnFailureListener {
                Log.e("ExerciseDAO", "Neuspješno dodavanje vježbi")
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

