package com.kostry.yourtimer.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.kostry.yourtimer.R
import com.kostry.yourtimer.di.provider.AppComponentProvider
import com.kostry.yourtimer.ui.mainactivity.MainActivity
import com.kostry.yourtimer.ui.mainactivity.MainActivity.Companion.NOTIFICATION_CHANNEL_ID
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
        initBroadcastReceiver()
        coroutineScope.launch {
            myTimer.timerState.collectLatest { state ->
                when (state) {
                    is TimerState.NotAttached -> {
                        log("onStartCommand -> TimerState.NotAttached")
                        sendNotification("Timer is not attached")
                    }
                    is TimerState.Running -> {
                        log("onStartCommand -> TimerState.Running: ${state.millis.millisToStringFormat()}")
                        sendNotification(state.millis.millisToStringFormat())
                        serviceSendBroadcast(state.millis)
                    }
                    is TimerState.Paused -> {
                        log("onStartCommand -> TimerState.Paused: ${state.millis.millisToStringFormat()}")
                        sendNotification(state.millis.millisToStringFormat())
                        serviceSendBroadcast(state.millis)
                    }
                    is TimerState.Finished -> {
                        log("onStartCommand -> TimerState.Finished")
                        sendNotification("Timer is finished")
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

    private fun initBroadcastReceiver() {
        log("initBroadcastReceiver")
        val intentFilter = IntentFilter()
        intentFilter.addAction(INTENT_FILTER_FRAGMENT_TIMER_SEND_BROADCAST)
        val broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                log("broadcastReceiver -> onReceive")
                val command: Int =
                    intent?.extras?.getInt(INTENT_EXTRA_KEY_FRAGMENT_TIMER_COMMAND) ?: 0
                val millis: Long =
                    intent?.extras?.getLong(INTENT_EXTRA_KEY_FRAGMENT_TIMER_MILLIS) ?: 0
                when (command) {
                    TIMER_START -> {
                        log("broadcastReceiver -> startTimer")
                        myTimer.startTimer(millis)
                    }
                    TIMER_PAUSE -> {
                        log("broadcastReceiver -> pauseTimer")
                        myTimer.pauseTimer(millis)
                    }
                }
            }
        }
        this.registerReceiver(broadcastReceiver, intentFilter)
    }

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "MyForegroundService: $message")
    }

    private fun sendNotification(message: String) {
        log("sendNotification")
        NotificationManagerCompat
            .from(this)
            .notify(NOTIFICATION_ID, createNotification(message))
    }

    private fun serviceSendBroadcast(millis: Long) {
        val intent = Intent(INTENT_FILTER_SERVICE_TIMER_SEND_BROADCAST)
        intent.putExtra(INTENT_EXTRA_KEY_SERVICE_TIMER, millis)
        sendBroadcast(intent)
    }

    private fun createNotification(message: String): Notification {
        log("createNotification")
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

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
        const val INTENT_FILTER_SERVICE_TIMER_SEND_BROADCAST =
            "INTENT_FILTER_SERVICE_TIMER_SEND_BROADCAST"
        const val INTENT_EXTRA_KEY_SERVICE_TIMER = "INTENT_EXTRA_KEY_SERVICE_TIMER"

        const val INTENT_FILTER_FRAGMENT_TIMER_SEND_BROADCAST =
            "INTENT_FILTER_FRAGMENT_TIMER_SEND_BROADCAST"
        const val INTENT_EXTRA_KEY_FRAGMENT_TIMER_COMMAND =
            "INTENT_EXTRA_KEY_FRAGMENT_TIMER_COMMAND"
        const val INTENT_EXTRA_KEY_FRAGMENT_TIMER_MILLIS = "INTENT_EXTRA_KEY_FRAGMENT_TIMER_MILLIS"

        const val TIMER_START = 1
        const val TIMER_PAUSE = 2

        private const val NOTIFICATION_ID = 1

        fun newIntent(context: Context): Intent {
            return Intent(context, TimerService::class.java)
        }
    }
}