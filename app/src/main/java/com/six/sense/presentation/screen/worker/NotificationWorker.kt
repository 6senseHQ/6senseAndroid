package com.six.sense.presentation.screen.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.core.app.NotificationCompat
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.six.sense.R
import java.util.concurrent.TimeUnit

/**
 * Worker class responsible for triggering notifications.
 * @property context [Context] The application context.
 * @property params [WorkerParameters] The worker parameters.
 */
class NotificationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        //triggering the notification.
        triggerNotification(applicationContext)
        return Result.success(workDataOf("message" to "successful"))
        // Schedule the next notification for force scheduling to testing purpose
        /* scheduleWork(applicationContext) */
    }

    private fun triggerNotification(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "example_channel_id"
        val channelName = "Example Channel"

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            if (notificationManager.getNotificationChannel(channelId) == null) {
                val channel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationManager.createNotificationChannel(channel)
            }
        }


        /**
         * Notification types for different scenarios
         */
        val notificationId = 1 // Ensure a single notification by using the same ID
        val resources = context.resources

        /**
         * Notification big text
         */
        val notificationBigText = NotificationCompat.Builder(context, channelId)
            .setContentTitle("WorkManager Notification")
            .setContentText("Notification triggered by WorkManager")
            .setSmallIcon(R.drawable.ic_send_outline)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(NotificationCompat.BigTextStyle().bigText( LoremIpsum(words = 30).values.joinToString(" ") ))
            .setOngoing(false) // Ensure notification is not persistent
            .build()

        /**
         * Notification multi line
         */
        val notificationMultiLine = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_send_outline)
            .setContentTitle("5 New mails from Frank")
            .setContentText("Check them out")
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_send_outline))
            .setStyle(
                NotificationCompat.InboxStyle()
                    .addLine("Re: Planning")
                    .addLine("Delivery on its way")
                    .addLine("Follow-up")
            )
            .build()


        /**
         * Regular Notification
         */
        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("WorkManager Notification")
            .setContentText("Notification triggered by WorkManager")
            .setSmallIcon(R.drawable.ic_send_outline)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOngoing(false) // Ensure notification is not persistent
            .build()

        notificationManager.notify(notificationId, notification)
    }

    /**
     * Need to experiment more on this behaviour.
     */
    /*private fun createForegroundInfo(): ForegroundInfo {
        val channelId = "foreground_channel_id"
        val channelName = "Foreground Service Channel"

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = applicationContext.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Running Task")
            .setContentText("WorkManager is running a background task.")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()

        return ForegroundInfo(2, notification) // ID must be unique
    }*/
}


/**
 * Schedules a work request to be executed.
 * @param context [Context] The application context.
 * @param repeatInterval [Long] The repeat interval in minutes.
 * @param timeUnit [TimeUnit] The time unit of the repeat interval.
 * @param initialDelay [Long] The initial delay in minutes.
 * @param initialTimeUnit [TimeUnit] The time unit of the initial delay.
 */
fun scheduleWork(
    context: Context,
    repeatInterval: Long = 15,
    timeUnit: TimeUnit = TimeUnit.MINUTES,
    initialDelay: Long = 0,
    initialTimeUnit: TimeUnit = TimeUnit.SECONDS,
) {
    /**
     * OneTime work request builder.
     *
     * The initial delay is for the first execution.
     */
    val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
        .setInitialDelay(initialDelay, initialTimeUnit)
        .setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .build()
        ).build()

    /**
     * Periodic work request builder.
     *
     * The default repeat interval is 15 Min and the least amount of time is 15 Min also.
     * Here After 15 min is set to trigger the notification again.
     * The initial delay is for the first execution.
     */
    val periodicWorkRequest =
        PeriodicWorkRequestBuilder<NotificationWorker>(repeatInterval, timeUnit)
            .setInitialDelay(initialDelay, initialTimeUnit)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                    .build()
            ).build()


    /* For periodic work request, also the following code can be used. */

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "ExamplePeriodicWork",
        ExistingPeriodicWorkPolicy.REPLACE,
        periodicWorkRequest
    )

    /* Enqueues the work request to be executed, the work will be replaced by existing one */

    WorkManager.getInstance(context).enqueueUniqueWork(
        "ExampleWork",
        ExistingWorkPolicy.REPLACE,
        workRequest
    )
}

/**
 * Sets up work manager on app start.
 * @param context [Context] The application context.
 */
fun setupWorkManagerOnAppStart(context: Context) {
    scheduleWork(
        context,
        repeatInterval = 15,
        timeUnit = TimeUnit.MINUTES,
        initialDelay = 0,
        initialTimeUnit = TimeUnit.SECONDS
    )
}