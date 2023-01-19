package com.example.myfitness.DataAccessObjects

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
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


    fun getExercise(bodyPart: String, db: FirebaseFirestore, callback: (List<Exercise>) -> Unit) {
        val exercisesRef = db.collection("exercises")
        exercisesRef
            .whereEqualTo("bodyPart", bodyPart)
            .limit(5) //TODO dodati stranicenje
            .get()
            .addOnSuccessListener { querySnapshot ->
                val exercises = querySnapshot.toObjects(Exercise::class.java)
                callback(exercises)
            }
            .addOnFailureListener {
                Log.e("ExerciseDAO", "Neuspješno dohvaćanje vježbe!")
            }
    }

    fun getAllExercises(db: FirebaseFirestore, callback: (List<Exercise>) -> Unit) {
        val exercisesRef = db.collection("exercises")
        exercisesRef
            .get()
            .addOnSuccessListener { querySnapshot ->
                val exercises = querySnapshot.documents.map { document ->
                    document.toObject(Exercise::class.java)!!
                }
                callback(exercises)
            }
            .addOnFailureListener {
                Log.e("ExerciseDAO", "Neuspješno dohvaćanje vježbi!")
            }
    }



    private val BODY_PARTS = listOf("Leđa", "Prsa", "Noge", "Ramena", "Bicepsi", "Tricepsi")
}

