package com.kostry.yourtimer.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.kostry.yourtimer.R
import com.kostry.yourtimer.di.provider.AppComponentProvider
import com.kostry.yourtimer.ui.mainactivity.MainActivity
import com.kostry.yourtimer.ui.mainactivity.MainActivity.Companion.NOTIFICATION_CHANNEL_ID
import com.kostry.yourtimer.ui.timer.TimerFragment
import com.kostry.yourtimer.util.MyTimer
import com.kostry.yourtimer.util.TimerState
import com.kostry.yourtimer.util.millisToStringFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class TimerService : Service() {

    @Inject
    lateinit var myTimer: MyTimer

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        (application as AppComponentProvider)
            .provideAppComponent()
            .inject(this)
        super.onCreate()
        startForeground(NOTIFICATION_ID, createNotification("Timer is not attached"))
        log("onCreate")

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand")
        coroutineScope.launch {
            myTimer.timerState.collectLatest { state ->
                when (state) {
                    is TimerState.Running -> {
                        log("onStartCommand -> TimerState.Running: ${state.millis.millisToStringFormat()}")
                        sendNotification(state.millis.millisToStringFormat())
                    }
                    is TimerState.Paused -> {
                        log("onStartCommand -> TimerState.Paused: ${state.millis.millisToStringFormat()}")
                        sendNotification(state.millis.millisToStringFormat())
                    }
                    is TimerState.Stopped -> {
                        log("onStartCommand -> TimerState.Finished")
                        sendNotification("Timer is finished")
                        stopSelf()
                    }
                }
            }
        }
        return START_NOT_STICKY
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

    private fun sendNotification(message: String) {
        log("sendNotification")
        NotificationManagerCompat
            .from(this)
            .notify(NOTIFICATION_ID, createNotification(message))
    }

    private fun createNotification(message: String): Notification {
        log("createNotification")

        val pendingIntent: PendingIntent = NavDeepLinkBuilder(this)
            .setGraph(R.navigation.main_nav_graph)
            .setDestination(R.id.timerFragment)
            .setArguments(args = bundleOf(TimerFragment.TIMER_FRAGMENT_ARGS_KEY to 99000L))
            .createPendingIntent()

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
            .setSilent(true)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setAutoCancel(true)
            .build()
    }

    companion object {
        private const val NOTIFICATION_ID = 1
        fun newIntent(context: Context) = Intent(context, TimerService::class.java)
    }
}