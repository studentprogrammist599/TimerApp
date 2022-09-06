package com.kostry.yourtimer.ui.mainactivity

interface MainActivityCallback {

    fun startTimerService(startMillis: Long)
    fun stopTimerService()
}