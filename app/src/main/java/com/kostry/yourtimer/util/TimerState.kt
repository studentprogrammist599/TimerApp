package com.kostry.yourtimer.util

sealed class TimerState {
    object NotAttached: TimerState()
    data class Running(val millis: Long): TimerState()
    data class Paused(val millis: Long): TimerState()
    object Finished: TimerState()
}