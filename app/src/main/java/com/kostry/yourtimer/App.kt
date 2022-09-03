package com.kostry.yourtimer

import android.app.Application
import com.kostry.yourtimer.di.component.AppComponent
import com.kostry.yourtimer.di.component.DaggerAppComponent
import com.kostry.yourtimer.di.component.HomeSubcomponent
import com.kostry.yourtimer.di.module.AppModule
import com.kostry.yourtimer.ui.home.HomeSubcomponentProvider

class App : Application(), HomeSubcomponentProvider {

    private val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    private var homeSubcomponent: HomeSubcomponent? = null

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
}