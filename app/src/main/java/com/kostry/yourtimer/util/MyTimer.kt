package com.kostry.yourtimer.util

import android.os.CountDownTimer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MyTimer {

    private var startTimeMillis: Long = 0L
    private var repsCount = 0
    private var timer: CountDownTimer? = null
    private val _timerState = MutableStateFlow<TimerState>(TimerState.Stopped)
    val timerState = _timerState.asStateFlow()

    fun runTimer(reps: Int, millis: Long){
        repsCount = reps
        startTimeMillis = millis
        startTimer(millis)
    }

    fun restartTimer(){
        if (timer == null && timerState.value is TimerState.Paused && repsCount != 0){
            startTimer((timerState.value as TimerState.Paused).millis)
        }
    }

    fun stopTimer(){
        repsCount = 0
        onFinishTimer()
    }

    fun pauseTimer() {
        if (timer != null && timerState.value is TimerState.Running) {
            timer?.cancel()
            timer = null
            _timerState.value = TimerState.Paused(repsCount, (timerState.value as TimerState.Running).millis)
        }
    }

    fun getStartTime(): Long {
        return startTimeMillis
    }

    private fun startTimer(millis: Long) {
        if (timer == null && timerState.value !is TimerState.Running && millis != 0L && repsCount != 0) {
            timer?.cancel()
            timer = null
            timer = object : CountDownTimer(millis, TIMER_INTERVAL) {
                override fun onTick(millisUntilFinished: Long) {
                    _timerState.value = TimerState.Running(repsCount, millisUntilFinished)
                }

                override fun onFinish() {
                    repsCount -= 1
                    _timerState.value = TimerState.OnFinished
                    onFinishTimer()
                }
            }.start()
        }
    }

    private fun onFinishTimer() {
        timer?.cancel()
        timer = null
        if (repsCount > 0){
            startTimer(startTimeMillis)
        }else {
            _timerState.value = TimerState.Stopped
        }
    }

    companion object {
        private const val TIMER_INTERVAL: Long = 1000L
    }
}