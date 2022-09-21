package com.kostry.yourtimer.util

import android.os.CountDownTimer
import com.kostry.yourtimer.datasource.models.PresetModel
import com.kostry.yourtimer.datasource.models.TimeCardModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MyTimer {

    private var presetModel: PresetModel? = null
    private var timeCard: TimeCardModel? = null
    private var startTimeMillis: Long = 0L
    private var repsCount = 0
    private var timer: CountDownTimer? = null
    private val _timerState = MutableStateFlow<TimerState>(TimerState.Stopped)
    val timerState = _timerState.asStateFlow()

    fun runTimer(preset: PresetModel){
        presetModel = preset
        timeCard = presetModel?.timeCards?.first()
        repsCount = timeCard?.reps ?: 0
        startTimeMillis = timeCard?.let {
            mapTimeToMillis(it.hours ?: 0, it.minutes ?: 0, it.seconds ?: 0)
        } ?: 0L
        startTimer(startTimeMillis)
    }

    fun restartTimer() {
        if (timer == null && timerState.value is TimerState.Paused) {
            startTimer((timerState.value as TimerState.Paused).millis)
        }
    }

    fun stopTimer() {
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
        if (repsCount > 0) {
            startTimer(startTimeMillis)
        } else {
            _timerState.value = TimerState.Stopped
        }
    }

    companion object {
        private const val TIMER_INTERVAL: Long = 1000L
    }
}