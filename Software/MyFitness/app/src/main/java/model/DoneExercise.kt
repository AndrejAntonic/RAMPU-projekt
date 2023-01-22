package model

import com.google.firebase.Timestamp
import java.util.*

data class DoneExercise(
    var exerciseName : String = "",
    var weight : Int = 0,
    var sets : Int = 0,
    var reps : Int = 0,
    var date : Timestamp = Timestamp.now()
)
