package model

import java.util.*

data class DoneExercise(
    val exerciseName : String,
    val weight : Int,
    val sets : Int,
    val reps : Int,
    val date : Date,
    val username : String,
)
