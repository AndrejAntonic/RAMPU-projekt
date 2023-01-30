package com.example.myfitness.entities

import com.google.firebase.Timestamp

data class User (
    var weight: Double = 0.0,
    var username: String = "",
    var password: String = "",
    var email: String = "",
    var height: Double = 0.0,
    var gender: String = "",
    var activity : String = "",
    var age : String = "",
    var session : Timestamp = Timestamp.now(),
    @JvmField var timestamp: Long = 0
)
