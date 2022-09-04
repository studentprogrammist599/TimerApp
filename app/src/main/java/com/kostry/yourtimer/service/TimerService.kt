package com.kostry.yourtimer.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.kostry.yourtimer.R
import com.kostry.yourtimer.ui.mainactivity.MainActivity.Companion.NOTIFICATION_CHANNEL_ID
import kotlinx.coroutines.*

class TimerService : Service() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
        startForeground(NOTIFICATION_ID, createNotification("0"))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand")
        val startMillis: Long = intent?.getLongExtra(START_MILLIS, 0) ?: 0
        coroutineScope.launch {
            for (i in startMillis..startMillis + 100) {
                delay(1000)
                log("Timer $i")
                NotificationManagerCompat
                    .from(this@TimerService)
                    .notify(NOTIFICATION_ID, createNotification(i.toString()))
            }
            stopSelf()
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        log("onDestroy")
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "MyForegroundService: $message")
    }

    private fun createNotification(time: String) = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        .setContentText(time)
        .setSmallIcon(R.drawable.ic_launcher_background)
        .build()

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val START_MILLIS = "START_MILLIS"

        fun newIntent(context: Context, startMillis: Long): Intent {
            return Intent(context, TimerService::class.java).apply {
                putExtra(START_MILLIS, startMillis)
            }
        }
    }
}