package com.example.myfitness.DataAccessObjects


import android.content.Context
import android.util.Log
import com.example.myfitness.utils.Hash
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import model.User


object UsersDAO {

     fun AddUser(username: String, email: String, password: String) : Task<DocumentReference> {
         val db = Firebase.firestore
         val user = hashMapOf(
             "username" to username,
             "email" to email,
             "password" to password
         )
         var result = false
         return db.collection("users").add(user)
    }

    suspend fun GetUserByUsername(username: String) : Boolean {
        val db = Firebase.firestore
        val user = db.collection("users")
            .whereEqualTo("username", username)
            .get().await()

        return user.size() > 0
    }

    suspend fun GetUserByEmail(email: String) : Boolean {
        val db = Firebase.firestore
        val user = db.collection("users")
            .whereEqualTo("email", email)
            .get().await()

        return user.size() > 0
    }

    suspend fun LoginUser(username: String, password: String) : Boolean {
        val db = Firebase.firestore
        val user = db.collection("users")
            .whereEqualTo("username", username)
            .whereEqualTo("password", password)
            .get().await()

        return user.size() > 0
    }

    suspend fun EditUser(context: Context, username: String, email: String, password: String, weight: Double) {
        val db = Firebase.firestore
        val currentUser = getCurrentUser(context)

        val userRef = db.collection("users").whereEqualTo("username", currentUser)
        val document = userRef.get().await()
        if (document.size() > 0) {
            val user = document.documents[0].reference
            val updates = HashMap<String, Any>()
            updates["username"] = username
            updates["email"] = email
            updates["weight"] = weight

            if (!password.isEmpty()) {
                updates["password"] = Hash.hashPassword(password)
            }
            user.update(updates).await()
        } else {
            Log.d("EditUser", "Nije moguce promijeniti podatke")
        }
    }


    fun getCurrentUser(context : Context) : String {
        val prefs = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        return prefs.getString("username", "")!!
    }

    suspend fun getUserInfo(context: Context) : MutableList<User> {
        val db = Firebase.firestore
        val userList = mutableListOf<User>()
        val currentUser = getCurrentUser(context)
        val users = db.collection("users").whereEqualTo("username", currentUser)
        try {
            val result = users.get().await()
            for (document in result) {
                val user = document.toObject(User::class.java)
                userList.add(user)
            }
            return userList
        } catch (e: Exception) {
            throw e
        }
    }

}
