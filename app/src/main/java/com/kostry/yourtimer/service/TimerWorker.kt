package com.kostry.yourtimer.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import com.kostry.yourtimer.R
import com.kostry.yourtimer.ui.mainactivity.MainActivity
import kotlinx.coroutines.delay

class TimerWorker(
    private val context: Context,
    private val workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters) {

    @SuppressLint("RestrictedApi")
    override suspend fun doWork(): Result {
        log("doWork")
        val startMillis: Long = workerParameters.inputData.getLong(START_MILLIS, 0)
        for (i in startMillis..startMillis + 100) {
            delay(1000)
            log("Timer $i")
                NotificationManagerCompat
                    .from(context)
                    .notify(NOTIFICATION_ID, createNotification(i.toString()))
        }
        return Result.success()
    }

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "TimerWorker: $message")
    }

    private fun createNotification(time: String): Notification {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context, MainActivity.NOTIFICATION_CHANNEL_ID)
            .setContentText(time)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
            .setSilent(true)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setAutoCancel(true)
            .build()
    }

    companion object {
        const val TIMER_WORKER_NAME = "TIMER_WORKER_NAME"
        private const val NOTIFICATION_ID = 1
        private const val START_MILLIS = "START_MILLIS"

        fun makeRequest(startMillis: Long): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<TimerWorker>()
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setInputData(workDataOf(START_MILLIS to startMillis))
                .build()
        }
    }
}