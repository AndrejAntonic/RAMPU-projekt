package com.example.myfitness.DataAccessObjects

import android.R
import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import model.Exercise
import java.io.ByteArrayOutputStream


object ExerciseDAO {

//    fun addExercise(exercise: Exercise, db: FirebaseFirestore) {
//        val exercisesRef = db.collection("exercises")
//        exercisesRef.add(exercise)
//            .addOnSuccessListener {
//                Log.d("ExerciseDAO", "Vježba dodana!")
//            }
//            .addOnFailureListener {
//                Log.e("ExerciseDAO", "Greška prilikom dodavanja vježbe!")
//            }
//    }

//    fun addExercise(exercise: Exercise, db: FirebaseFirestore, image: Bitmap) {
//        val exercisesRef = db.collection("exercises")
//        val baos = ByteArrayOutputStream()
//        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
//        val imageBytes = baos.toByteArray()
//        val imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT)
//        val data = hashMapOf(
//            "name" to exercise.name,
//            "description" to exercise.description,
//            "difficulty" to exercise.difficulty,
//            "equipment" to exercise.equipment,
//            "bodyType" to exercise.bodyType,
//            "image" to imageString
//        )
//        exercisesRef.document(exercise.name).set(data)
//            .addOnSuccessListener {
//                Log.d("ExerciseDAO", "Vježba dodana!")
//            }
//            .addOnFailureListener {
//                Log.e("ExerciseDAO", "Greška prilikom dodavanja vježbe!")
//            }
//    }

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

    fun fillSpinner(spinner: Spinner) {
        spinner.adapter = ArrayAdapter(
            spinner.context,
            R.layout.simple_spinner_item,
            BODY_PARTS
        )
    }

    suspend fun getAllExercises() : MutableList<Exercise> {
        val db = Firebase.firestore
        val exercisesList = mutableListOf<Exercise>()

        val exercises = db.collection("exercises")

        try {
            val result = exercises.get().await()
            for (document in result) {
                val exercise = document.toObject(Exercise::class.java)
                println("Exercise:")
                println(exercise)
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

