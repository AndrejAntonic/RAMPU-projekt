package com.example.myfitness.helpers

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat

object DateHelper {
    fun getMonth(timestamp : Timestamp) : String {
        val format = "MM"
        val sdf = SimpleDateFormat(format)
        return sdf.format(timestamp.toDate())
    }

    fun getDate(timestamp : Timestamp) : String {
        val format = "dd"
        val sdf = SimpleDateFormat(format)
        return sdf.format(timestamp.toDate())
    }

    fun getYear(timestamp: Timestamp) : String {
        val format = "yyyy"
        val sdf = SimpleDateFormat(format)
        return sdf.format(timestamp.toDate())
    }
 }