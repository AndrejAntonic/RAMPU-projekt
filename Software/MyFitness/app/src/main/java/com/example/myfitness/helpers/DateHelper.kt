package com.example.myfitness.helpers

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat

object DateHelper {
    fun getMonth(timestamp : Timestamp) : String {
        val monthFormat = "MM"
        val sdf = SimpleDateFormat(monthFormat)
        return sdf.format(timestamp.toDate())
    }

    fun getDate(timestamp : Timestamp) : String {
        val dayFormat = "dd"
        val sdf = SimpleDateFormat(dayFormat)
        return sdf.format(timestamp.toDate())
    }
}