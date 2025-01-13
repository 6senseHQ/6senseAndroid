package com.six.sense.presentation

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.six.sense.R
import kotlin.random.Random

/**
 * Service responsible for handling Firebase Cloud Messaging (FCM) notifications.
 */
class FCMService : FirebaseMessagingService() {

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "6sense Alert",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Timber.tag(TAG).d("Refreshed token: %s", token)
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        //sendRegistrationToServer(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val topic = remoteMessage.from.toString().removePrefix("/topics/")
//        topic.com.six.sense.utils.log(TAG)
//        remoteMessage.data.logJson(TAG)

        sendNotification(remoteMessage.data)

    }

    private fun sendNotification(messageBody: Map<String, String>) {
        val title = messageBody["title"] ?: ""
        val body = messageBody["body"] ?: ""

        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP )
        }
        val pendingIntent = PendingIntent.getActivity(this, randomId, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat.from(this)
                .notify(randomId, notificationBuilder.build())
        }
    }


    private val randomId
        get() =
            System.nanoTime().toInt().plus(Random(1000).nextInt())


    companion object {
        private const val TAG = "FirebaseNotificationService"
        private const val CHANNEL_ID = "Six Sense Alert"
    }
}