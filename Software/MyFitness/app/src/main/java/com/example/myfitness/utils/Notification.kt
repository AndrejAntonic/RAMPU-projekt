package com.example.myfitness.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.myfitness.R
import com.example.myfitness.StartActivity

class Notification(
    private val context: Context
) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification() {
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

}