package com.kostry.yourtimer.ui.mainactivity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.kostry.yourtimer.R
import com.kostry.yourtimer.di.provider.AppComponentProvider
import com.kostry.yourtimer.service.TimerService
import com.kostry.yourtimer.util.sharedpref.SharedPrefsRepository
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPrefsRepository: SharedPrefsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as AppComponentProvider)
            .provideAppComponent()
            .inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()
        if (sharedPrefsRepository.timerServiceIsActive != true){
            sharedPrefsRepository.timerServiceIsActive = true
            ContextCompat.startForegroundService(
                this,
                TimerService.newIntent(this, 25)
            )
        }
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