package com.kostry.yourtimer.ui.timer

import com.kostry.yourtimer.di.component.TimerSubcomponent

interface TimerSubcomponentProvider {

    fun initTimerSubcomponent(): TimerSubcomponent
    fun destroyTimerSubcomponent()
}