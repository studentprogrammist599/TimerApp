package com.kostry.yourtimer.util

import android.os.CountDownTimer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MyTimer {

    private var timer: CountDownTimer? = null
    private val _timerState = MutableStateFlow<TimerState>(TimerState.NotAttached)
    val timerState = _timerState.asStateFlow()

    fun startTimer(millis: Long) {
        if (timer == null) {
            timer = object : CountDownTimer(millis, TIMER_INTERVAL) {
                override fun onTick(millisUntilFinished: Long) {
                    _timerState.value = TimerState.Running(millisUntilFinished)
                }

                override fun onFinish() {
                    _timerState.value = TimerState.Finished
                }
            }.start()
        }
    }

    fun pauseTimer(millis: Long) {
        if (timer != null) {
            _timerState.value = TimerState.Paused(millis)
            timer?.cancel()
            timer = null
        }
    }

    companion object {
        private const val TIMER_INTERVAL: Long = 1000L
    }
}