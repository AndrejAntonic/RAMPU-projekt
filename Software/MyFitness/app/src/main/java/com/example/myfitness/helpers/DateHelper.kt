package com.example.myfitness.helpers

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

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

    fun getFirstDayOfMonth(month: String): Timestamp {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = dateFormat.parse("$month-01")
        return Timestamp(date)
    }

    fun hasTimePassed(date: Date): Boolean {
        val now = Date()
        return date.before(now)
    }
 }