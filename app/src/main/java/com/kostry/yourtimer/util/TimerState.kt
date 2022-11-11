package com.kostry.yourtimer.util

sealed class TimerState {
    data class Running(val reps: Int, val seconds: Long): TimerState()
    data class Paused(val reps: Int, val seconds: Long): TimerState()
    object Stopped: TimerState()
}