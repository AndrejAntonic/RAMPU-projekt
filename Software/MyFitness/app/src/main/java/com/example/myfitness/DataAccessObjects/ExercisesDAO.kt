package com.example.myfitness.DataAccessObjects

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import com.example.myfitness.entities.DailyExercises
import com.example.myfitness.entities.DoneExercise
import com.example.myfitness.entities.Exercises
import com.example.myfitness.entities.Plan
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.security.Timestamp

object ExercisesDAO {
    suspend fun getExercise(bodyPart: String, difficulty: Int): MutableList<Exercises> {
        val db = Firebase.firestore
        val exercisesList = mutableListOf<Exercises>()

        val exercise = db.collection("exercises")
        val query = exercise.whereEqualTo("bodyType", bodyPart).whereLessThanOrEqualTo("difficulty", difficulty)

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

    suspend fun getPlan(username: String) : MutableList<Plan> {
        val plan = mutableListOf<Plan>()
        val days = arrayOf("Ponedjeljak", "Utorak", "Srijeda", "Četvrtak", "Petak", "Subota", "Nedjelja")

        return try {
            days.forEach {
                var lista = getListFromDB(it, username).get().await()
                for(document in lista) {
                    plan.add(Plan(it, getElements(document)))
                }
            }

            plan
        } catch (e: Exception) {
            mutableListOf()
        }
    }

    fun getListFromDB(day: String, username: String): Query {
        val db = Firebase.firestore
        return db.collection("workoutPlan").document(username).collection(day).orderBy("timeStamp", Query.Direction.DESCENDING).limit(1)
    }


    //suspend znaci da se funkcija moze pauzirati i kasnije opet nastaviti (kako bi drugi dijelovi koda mogli obaviti svoje)
    suspend fun getDaily(username: String, datePicked: String) : MutableList<DailyExercises> {
        val dailyPlan = mutableListOf<DailyExercises>()

        return try {
            //salje izabrani datum i trenutnog korisnika u getListFromDBDaily
            val listFromDB = getListFromDBDaily(datePicked, username).get().await()
            //za svaki dokument u listi konverta dokument u objekt DoneExercise
            for(document in listFromDB) {
                //Log.d(TAG, "${document.id} => ${document.data}")
                val exercise = document.toObject(DailyExercises::class.java)
                //svaki konvertani objekt dodaje u listu dailyPlan
                dailyPlan.add(exercise)

                //Log.d("proba", "$dailyPlan")

            }
            dailyPlan
        } catch (e: Exception) {
            //ako se dogodi greska vraca praznu listu
            mutableListOf()
        }
    }


    fun getListFromDBDaily(datePicked: String, username: String): Query {
        val db = Firebase.firestore
        //filtrira podatke koje ce dohvacati po usernameu i datumu i vraca ih
        return db.collection("dailyPlan").document(username).collection("savedDailyPlan").whereEqualTo("date", datePicked)
    }


    fun getElements(document: QueryDocumentSnapshot): MutableList<Exercises> {
        val tempList = mutableListOf<Exercises>()
        var proba = false
        var inkrement = 0
        do {
            inkrement++
            if(document.getString("vjezba$inkrement").toString() != "null")
                tempList.add(Exercises(document.getString("vjezba$inkrement").toString()))
            else
                proba = true
        }while (!proba)

        return tempList
    }
}