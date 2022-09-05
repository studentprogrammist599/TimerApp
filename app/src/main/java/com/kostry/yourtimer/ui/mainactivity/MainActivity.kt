package com.kostry.yourtimer.ui.mainactivity

import android.app.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kostry.yourtimer.R
import com.kostry.yourtimer.broadcastreceiver.AlarmReceiver
import com.kostry.yourtimer.service.TimerService
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        createNotificationChannel()
//        if (!isTimerServiceRunning()) {
//            ContextCompat.startForegroundService(
//                this,
//                TimerService.newIntent(this)
//            )
//        }
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.SECOND, 10)
        val intent = AlarmReceiver.newIntent(this)
        val pendingIntent = PendingIntent.getBroadcast(this, 100, intent, PendingIntent.FLAG_MUTABLE)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
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