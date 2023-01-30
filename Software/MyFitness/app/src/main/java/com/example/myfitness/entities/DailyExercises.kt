package com.example.myfitness.entities

import com.google.firebase.Timestamp
import java.util.*

data class DailyExercises(
    var exerciseName : String = "",
    var weight : Int = 0,
    var sets : Int = 0,
    var reps : Int = 0,
    var date : String = ""
)
