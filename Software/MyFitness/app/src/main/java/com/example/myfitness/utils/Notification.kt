package com.example.myfitness.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.myfitness.R
import com.example.myfitness.StartActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Calendar

class Notification(
    private val context: Context
) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification(days: List<String>) {
        var selectedDays = getSelectedDays(days)
        GlobalScope.launch {
            while(isActive) {
                val calendar = Calendar.getInstance()

                if(selectedDays.contains(calendar.get(Calendar.DAY_OF_WEEK))) {
                    val intent = Intent(context, StartActivity::class.java)
                    val pendingIntent = PendingIntent.getActivity(context, 1, intent, if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
                    val notification = NotificationCompat.Builder(context, "id_kanala")
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle("Vrijeme je za vježbanje!")
                        .setContentText("Uđite u aplikaciju i zabilježite odrađene vježbe.")
                        .setContentIntent(pendingIntent)
                        .build()

                    notificationManager.notify(1, notification)
                }
                delay(1000 * 60 * 60 * 24)
            }
        }
    }

    private fun getSelectedDays(days: List<String>): Array<Int> {
        var selectedDays = arrayOf<Int>()
        days.forEach {
            when(it) {
                "Ponedjeljak" -> selectedDays += Calendar.MONDAY
                "Utorak" -> selectedDays += Calendar.TUESDAY
                "Srijeda" -> selectedDays += Calendar.WEDNESDAY
                "Četvrtak" -> selectedDays += Calendar.THURSDAY
                "Petak" -> selectedDays += Calendar.FRIDAY
                "Subota" -> selectedDays += Calendar.SATURDAY
                "Nedjelja" -> selectedDays += Calendar.SUNDAY
            }
        }
        return selectedDays
    }

}