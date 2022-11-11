package com.kostry.yourtimer.service

import android.app.Notification
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.kostry.yourtimer.R
import com.kostry.yourtimer.broadcastreceiver.AlarmReceiver
import com.kostry.yourtimer.di.provider.AppComponentProvider
import com.kostry.yourtimer.ui.mainactivity.MainActivity.Companion.NOTIFICATION_CHANNEL_ID
import com.kostry.yourtimer.util.MyTimer
import com.kostry.yourtimer.util.TIMER_NOTIFICATION_ID
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
        startForeground(
            TIMER_NOTIFICATION_ID,
            createNotification(TIMER_IS_STOPPED.toInt(), TIMER_IS_STOPPED)
        )
        log("onCreate")

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand")
        coroutineScope.launch {
            myTimer.timerState.collectLatest { state ->
                when (state) {
                    is TimerState.Running -> {
                        log("onStartCommand -> TimerState.Running: ${state.millis.millisToStringFormat()}")
                        sendNotification(state.reps, state.millis)
                    }
                    is TimerState.Paused -> {
                        log("onStartCommand -> TimerState.Paused: ${state.millis.millisToStringFormat()}")
                        sendNotification(state.reps, state.millis)
                    }
                    is TimerState.Stopped -> {
                        log("onStartCommand -> TimerState.Finished")
                        sendBroadcast(AlarmReceiver.newIntent(this@TimerService))
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

    private fun sendNotification(reps: Int, timeMillis: Long) {
        log("sendNotification")
        NotificationManagerCompat
            .from(this)
            .notify(TIMER_NOTIFICATION_ID, createNotification(reps, timeMillis))
    }

    private fun createNotification(reps: Int, timeMillis: Long): Notification {
        log("createNotification")
        initBroadcastReceiver()

        val stopIntent = Intent(ACTION_FROM_NOTIFICATION).apply {
            putExtra(TIMER_COMMAND_EXTRA, TIMER_COMMAND_STOP)
        }
        val stopPendingAction = PendingIntent.getBroadcast(
            this,
            0,
            stopIntent,
            FLAG_IMMUTABLE
        )

        val pendingIntent: PendingIntent = NavDeepLinkBuilder(this)
            .setGraph(R.navigation.main_nav_graph)
            .setDestination(R.id.timerFragment)
            .createPendingIntent()

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentText(timeMillis.millisToStringFormat())
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
            .setSilent(true)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setAutoCancel(true)
            .addAction(
                R.drawable.ic_baseline_close_24,
                applicationContext.getString(R.string.stop),
                stopPendingAction
            )
            .build()
    }

    private fun initBroadcastReceiver() {
        log("initBroadcastReceiver")
        val intentFilter = IntentFilter()
        intentFilter.addAction(ACTION_FROM_NOTIFICATION)
        val broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                log("broadcastReceiver -> onReceive")
                val command: Int =
                    intent?.extras?.getInt(TIMER_COMMAND_EXTRA) ?: 0
                when (command) {
                    TIMER_COMMAND_START_PAUSE -> {
                        log("broadcastReceiver -> TIMER_START_PAUSE")
                        when (myTimer.timerState.value) {
                            is TimerState.Running -> {
                                myTimer.pauseTimer()
                            }
                            is TimerState.Paused -> {
                                myTimer.restartTimer()
                            }
                            is TimerState.Stopped -> {}
                        }
                    }
                    TIMER_COMMAND_STOP -> {
                        log("broadcastReceiver -> TIMER_STOP")
                        myTimer.stopTimer()
                    }
                }
            }
        }
        this.registerReceiver(broadcastReceiver, intentFilter)
    }

    companion object {

        private const val ACTION_FROM_NOTIFICATION = "ACTION_FROM_NOTIFICATION"
        private const val TIMER_COMMAND_EXTRA = "TIMER_COMMAND_EXTRA"
        private const val TIMER_COMMAND_START_PAUSE = 1
        private const val TIMER_COMMAND_STOP = 2
        private const val TIMER_IS_STOPPED: Long = 0L
        fun newIntent(context: Context) = Intent(context, TimerService::class.java)
    }
}