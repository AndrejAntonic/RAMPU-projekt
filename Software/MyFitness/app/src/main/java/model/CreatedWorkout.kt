package model

import com.google.firebase.Timestamp
import java.util.*

data class CreatedWorkout(
    var exerciseName : String = "",
    var date : Timestamp = Timestamp.now()
)
