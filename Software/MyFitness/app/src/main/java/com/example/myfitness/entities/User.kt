package com.example.myfitness.entities

data class User (
    var weight: Double = 0.0,
    var username: String = "",
    var password: String = "",
    var email: String = "",
    var height: Double = 0.0,
    var gender: String = "",
    @JvmField var timestamp: Long = 0
)
