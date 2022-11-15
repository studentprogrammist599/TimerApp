package com.kostry.yourtimer.util

sealed class TimerState {
    data class Running(val reps: Int, val millis: Long, val cardName: String): TimerState()
    data class Paused(val reps: Int, val millis: Long, val cardName: String): TimerState()
    object Stopped: TimerState()
}