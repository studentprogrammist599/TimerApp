package com.kostry.yourtimer.di.provider

import com.kostry.yourtimer.di.component.TimerSubcomponent

interface TimerSubcomponentProvider {

    fun initTimerSubcomponent(): TimerSubcomponent
    fun destroyTimerSubcomponent()
}