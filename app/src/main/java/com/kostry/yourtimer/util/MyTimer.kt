package com.kostry.yourtimer.util

import com.kostry.yourtimer.datasource.models.PresetModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MyTimer {

    private var presetModel: PresetModel? = null
    private val _timerState = MutableStateFlow<TimerState>(TimerState.Stopped)
    val timerState = _timerState.asStateFlow()

    fun runTimer(preset: PresetModel) {
        presetModel = preset
        startTimer()
    }

    fun restartTimer() {

    }

    fun stopTimer() {

    }

    fun pauseTimer() {

    }


    private fun startTimer() {

    }

    companion object {
        private const val TIMER_INTERVAL: Long = 1000L
    }
}