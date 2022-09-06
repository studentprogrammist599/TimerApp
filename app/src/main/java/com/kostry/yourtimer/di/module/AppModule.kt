package com.kostry.yourtimer.di.module

import android.app.Application
import com.kostry.yourtimer.util.MyTimer
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    @Singleton
    @Provides
    fun provideApplication(): Application {
        return application
    }

    @Singleton
    @Provides
    fun provideMyTimer(): MyTimer = MyTimer()
}