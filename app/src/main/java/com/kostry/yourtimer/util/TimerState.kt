package com.kostry.yourtimer.util

sealed class TimerState {
    data class Running(val millis: Long): TimerState()
    data class Paused(val millis: Long): TimerState()
    object Stopped: TimerState()
}