package com.kostry.yourtimer.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.kostry.yourtimer.R
import com.kostry.yourtimer.di.provider.AppComponentProvider
import com.kostry.yourtimer.ui.mainactivity.MainActivity
import com.kostry.yourtimer.ui.mainactivity.MainActivity.Companion.NOTIFICATION_CHANNEL_ID
import com.kostry.yourtimer.util.TimerState
import com.kostry.yourtimer.util.millisToStringFormat
import com.kostry.yourtimer.util.sharedpref.SharedPrefsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class TimerService : Service() {

    @Inject
    lateinit var sharedPrefsRepository: SharedPrefsRepository

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var timer: CountDownTimer? = null
    private val timerState = MutableStateFlow<TimerState>(TimerState.NotAttached)

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
            timerState.collectLatest { state ->
                when (state) {
                    is TimerState.NotAttached -> {
                        sendNotification("Timer is not attached")
                    }
                    is TimerState.Running -> {
                        sendNotification((timerState.value as TimerState.Running).millis.millisToStringFormat())
                    }
                    is TimerState.Paused -> {
                        sendNotification((timerState.value as TimerState.Paused).millis.millisToStringFormat())
                    }
                    is TimerState.Finished -> {
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
                        startTimer(millis)
                    }
                    TIMER_PAUSE -> {
                        log("broadcastReceiver -> pauseTimer")
                        pauseTimer(millis)
                    }
                }
            }
        }
        this.registerReceiver(broadcastReceiver, intentFilter)
    }

    private fun startTimer(millis: Long) {
        log("startTimer")
        if (timer == null) {
            log("startTimer -> timer init")
            timer = object : CountDownTimer(millis, TIMER_INTERVAL) {
                override fun onTick(millisUntilFinished: Long) {
                    timerState.value = TimerState.Running(millisUntilFinished)
                    log("startTimer -> onTick -> ${millisUntilFinished.millisToStringFormat()}")
                    serviceSendBroadcast(millisUntilFinished)
                }

                override fun onFinish() {
                    timerState.value = TimerState.Finished
                    log("startTimer -> finished")
                }
            }.start()
        }
    }

    private fun pauseTimer(millis: Long) {
        log("pauseTimer")
        if (timer != null) {
            timerState.value = TimerState.Paused(millis)
            log("pauseTimer -> TimerState.Paused(${millis.millisToStringFormat()})")
            timer?.cancel()
            log("pauseTimer -> timer cancel")
            timer = null
            log("pauseTimer -> timer = null")
            serviceSendBroadcast(millis)
        }
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

        private const val TIMER_INTERVAL = 1000L
        private const val NOTIFICATION_ID = 1

        fun newIntent(context: Context): Intent {
            return Intent(context, TimerService::class.java)
        }
    }
}