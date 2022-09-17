package com.kostry.yourtimer.di.component

import com.kostry.yourtimer.di.module.AppModule
import com.kostry.yourtimer.di.module.RoomModule
import com.kostry.yourtimer.di.module.ViewModelModule
import com.kostry.yourtimer.service.TimerService
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ViewModelModule::class,
        RoomModule::class,
    ]
)
interface AppComponent {

    fun provideHomeSubcomponent(): HomeSubcomponent
    fun providePresetSubcomponent(): PresetSubcomponent
    fun inject(service: TimerService)
}