package com.example.myfitness.DataAccessObjects

import android.content.ContentValues.TAG
import android.util.Log
import com.example.myfitness.entities.Exercises
import com.example.myfitness.entities.Plan
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

object ExercisesDAO {
    suspend fun getExercise(bodyPart: String, difficulty: Int): MutableList<Exercises> {
        //Dohvaćanje vježbi iz baze podataka po atributima dio tijela i težina
        val db = Firebase.firestore
        val exercisesList = mutableListOf<Exercises>()

        val exercise = db.collection("exercises")
        val query = exercise.whereEqualTo("bodyType", bodyPart).whereLessThanOrEqualTo("difficulty", difficulty)

        try {
            val result = query.get().await()
            for(temp in result) {
                exercisesList.add(Exercises(temp.id)) //Dodavanje dohvaćene vježbe u listu
                Log.d(TAG, "${temp.id} => ${temp.data}") //Ispis vježbe i pojedinosti o vježbi
            }

            return exercisesList
        } catch (e: Exception) {
            //Ukoliko dođe do greške
            Log.w(TAG, "Error getting documents: ", e)

            return exercisesList
        }
    }

    fun addPlan(plan: List<Plan>, username: String) : Boolean {
        //Dodavanje generiranog plana treninga u bazu
        val db = Firebase.firestore
        for(day in plan) {
            var map: HashMap<String, Any> = HashMap() //Kreiranje hash mape pomoću koje će se dodavati u bazu
            map["timeStamp"] = FieldValue.serverTimestamp() //Generiranje datuma i vremena kada je plan napravljen
            var idk = 1
            for(exercises in day.exercise) {
                map["vjezba$idk"] = exercises.exercise //Spremanje vježbe u hash mapu
                idk++
            }
            //Spremanje u bazu
            db.collection("workoutPlan").document(username).collection(day.day).add(map)
        }

        return true
    }

    suspend fun getPlan(username: String) : MutableList<Plan> {
        //Dohvaćanje plana treninga iz baze podataka
        val plan = mutableListOf<Plan>()
        val days = arrayOf("Ponedjeljak", "Utorak", "Srijeda", "Četvrtak", "Petak", "Subota", "Nedjelja")

        return try {
            days.forEach {
                var lista = getListFromDB(it, username).get().await()
                for(document in lista) {
                    plan.add(Plan(it, getExerciseElements(document)))
                }
            }

            plan
        } catch (e: Exception) {
            mutableListOf()
        }
    }

    /*
    suspend fun getDays(username: String) : MutableList<String> {
        val planDays = mutableListOf<String>()
        val days = arrayOf("Ponedjeljak", "Utorak", "Srijeda", "Četvrtak", "Petak", "Subota", "Nedjelja")

        return try {
            days.forEach {
                var lista = getListFromDB(it, username).get().await()
                for(document in lista) {
                    if(getPlanDays(document))
                        planDays.add(it)
                }
            }

            planDays
        } catch (e: Exception) {
            mutableListOf()
        }
    }

    private fun getPlanDays(document: QueryDocumentSnapshot): Boolean {
        //Određivanje je li za prosljeđeni dan postoje vježe ili se radi o danu odmora
        return document.getString("vjezba1").toString() != "Odmor"
    }
     */

    private fun getListFromDB(day: String, username: String): Query {
        //Dohvaćanje plana za korisnika i određeni dan sortirano silazno po vremenu kreiranja plana
        val db = Firebase.firestore
        return db.collection("workoutPlan").document(username).collection(day).orderBy("timeStamp", Query.Direction.DESCENDING).limit(1)
    }

    private fun getExerciseElements(document: QueryDocumentSnapshot): MutableList<Exercises> {
        //Dohvaćanje samo vježbi iz plana treninga
        //Mora se raditi pošto u listi dohvaćenoj iz baze postoji element datum
        //Kako se on ne bi prikazivao u milisekundama u listu se spremaju samo vježbe
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