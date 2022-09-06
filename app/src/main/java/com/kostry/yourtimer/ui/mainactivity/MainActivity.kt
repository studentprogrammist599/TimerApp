package com.kostry.yourtimer.ui.mainactivity

import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.kostry.yourtimer.R
import com.kostry.yourtimer.service.TimerService

class MainActivity : AppCompatActivity(), MainActivityCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()
    }

    override fun startTimerService() {
        if (!isTimerServiceRunning() && !isChangingConfigurations && !isInMultiWindowMode) {
            ContextCompat.startForegroundService(
                applicationContext,
                TimerService.newIntent(applicationContext)
            )
        }
    }

    override fun stopTimerService() {
        stopService(TimerService.newIntent(applicationContext))
    }

    private fun isTimerServiceRunning(): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (TimerService::class.java.name == service.service.className) {
                return true
            }
        }
        return false
    }

    private fun createNotificationChannel() {
        val name = getString(R.string.app_name)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "NOTIFICATION_CHANNEL_ID"
    }
}