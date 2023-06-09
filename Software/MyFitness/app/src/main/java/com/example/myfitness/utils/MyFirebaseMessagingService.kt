package com.example.myfitness.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.myfitness.R
import com.example.myfitness.StartActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


const val channelID = "notification_channel"
const val channelName = "com.example.myfitness.utils"
class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        if(message.notification != null) {
            generateNotification(message.notification!!.title!!, message.notification!!.body!!)
        }
    }

    fun generateNotification(title: String, message: String) {
        val intent = Intent(this, StartActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channelID)
            .setSmallIcon(R.drawable.notification_icon)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
        
        builder = builder.setContent(getView(title, message))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0, builder.build())
    }

    private fun getView(title: String, message: String): RemoteViews {
        val remoteView = RemoteViews("com.example.myfitness.utils", R.layout.notification)

        remoteView.setTextViewText(R.id.notif_title, title)
        remoteView.setTextViewText(R.id.notif_description, message)
        remoteView.setImageViewResource(R.id.notif_img, R.drawable.notification_icon)

        return remoteView
    }
}