package com.kostry.yourtimer.util

import android.os.CountDownTimer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MyTimer {

    private var timer: CountDownTimer? = null
    private val _timerState = MutableStateFlow<TimerState>(TimerState.Stopped)
    val timerState = _timerState.asStateFlow()

    fun startTimer(millis: Long) {
        if (timerState.value !is TimerState.Running) {
            timer = object : CountDownTimer(millis, TIMER_INTERVAL) {
                override fun onTick(millisUntilFinished: Long) {
                    _timerState.value = TimerState.Running(millisUntilFinished)
                }

                override fun onFinish() {
                    stopTimer()
                }
            }.start()
        }
    }

    fun pauseTimer(millis: Long) {
        if (timerState.value !is TimerState.Paused) {
            _timerState.value = TimerState.Paused(millis)
            timer?.cancel()
        }
    }

    fun stopTimer(){
        timer?.cancel()
        timer = null
        _timerState.value = TimerState.Stopped
    }

    companion object {
        private const val TIMER_INTERVAL: Long = 1000L
    }
}