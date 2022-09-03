package com.kostry.yourtimer.di.component

import com.kostry.yourtimer.di.annotation.TimerScope
import com.kostry.yourtimer.di.module.TimerModule
import com.kostry.yourtimer.ui.timer.TimerFragment
import dagger.Subcomponent

@TimerScope
@Subcomponent(modules = [TimerModule::class])
interface TimerSubcomponent {

    fun inject(timerFragment: TimerFragment)
}