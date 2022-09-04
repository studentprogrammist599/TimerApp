package com.kostry.yourtimer.di.component

import com.kostry.yourtimer.di.module.AppModule
import com.kostry.yourtimer.di.module.ViewModelModule
import com.kostry.yourtimer.service.TimerService
import com.kostry.yourtimer.ui.mainactivity.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ViewModelModule::class,
    ]
)
interface AppComponent {

    fun provideHomeSubcomponent(): HomeSubcomponent
    fun inject(mainActivity: MainActivity)
    fun inject(service: TimerService)
}