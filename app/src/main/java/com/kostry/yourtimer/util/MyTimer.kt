package com.kostry.yourtimer.util

import com.kostry.yourtimer.datasource.models.PresetModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MyTimer {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var presetModel: PresetModel? = null
    private val _timerState = MutableStateFlow<TimerState>(TimerState.Stopped)
    val timerState = _timerState.asStateFlow()

    fun runTimer(preset: PresetModel) {
        presetModel = preset
        startTimer()
    }

    fun restartTimer() {
        startTimer()
    }

    fun stopTimer() {
        _timerState.value = TimerState.Stopped
        coroutineScope.coroutineContext.cancelChildren()
    }

    fun pauseTimer() {
        _timerState.value = TimerState.Paused(getActualReps(), getActualMillis())
        coroutineScope.coroutineContext.cancelChildren()
    }

    private fun startTimer() {
        coroutineScope.launch {
            _timerState.value = TimerState.Running(getActualReps(), getActualMillis())
            while (_timerState.value is TimerState.Running) {
                for (r in getActualReps() downTo 0) {
                    for (t in getActualMillis() downTo 0){
                        _timerState.value = TimerState.Running(r, t)
                        delay(TIMER_INTERVAL)
                    }
                }
            }
        }
    }

    private fun getActualReps(): Int {
        return presetModel?.timeCards?.first()?.reps ?: 0
    }

    private fun getActualMillis(): Long {
        return presetModel?.timeCards?.first()?.let { timeCard ->
            mapTimeToSeconds(
                hour = timeCard.hours ?: 0,
                minutes = timeCard.minutes ?: 0,
                seconds = timeCard.seconds ?: 0
            )
        } ?: 0L
    }

    companion object {
        private const val TIMER_INTERVAL: Long = 1000L
    }
}