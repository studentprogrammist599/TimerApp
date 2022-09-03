package com.kostry.yourtimer

import android.app.Application
import com.kostry.yourtimer.di.component.AppComponent
import com.kostry.yourtimer.di.component.DaggerAppComponent
import com.kostry.yourtimer.di.component.HomeSubcomponent
import com.kostry.yourtimer.di.component.TimerSubcomponent
import com.kostry.yourtimer.di.module.AppModule
import com.kostry.yourtimer.ui.home.HomeSubcomponentProvider
import com.kostry.yourtimer.ui.timer.TimerSubcomponentProvider

class App : Application(), HomeSubcomponentProvider, TimerSubcomponentProvider {

    private val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    private var homeSubcomponent: HomeSubcomponent? = null
    private var timerSubcomponent: TimerSubcomponent? = null

    override fun initHomeSubcomponent() = appComponent
        .provideHomeSubcomponent()
        .also {
            if (homeSubcomponent == null) {
                homeSubcomponent = it
            }
        }

    override fun destroyHomeSubcomponent() {
        homeSubcomponent = null
    }

    override fun initTimerSubcomponent() = appComponent
        .provideHomeSubcomponent()
        .provideTimerSubcomponent()
        .also { timerSubcomponent = it }

    override fun destroyTimerSubcomponent() {
        timerSubcomponent = null
    }
}