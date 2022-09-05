package com.kostry.yourtimer.di.module

import android.app.Application
import android.content.Context
import com.kostry.yourtimer.util.MyTimer
import com.kostry.yourtimer.util.sharedpref.SharedPrefsRepository
import com.kostry.yourtimer.util.sharedpref.SharedPrefsRepositoryImpl
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
    fun provideSharedPrefsRepository(
        application: Application
    ): SharedPrefsRepository{
        return SharedPrefsRepositoryImpl(
            application.getSharedPreferences("SHARED", Context.MODE_PRIVATE)
        )
    }

    @Provides
    fun provideMyTimer(): MyTimer = MyTimer()
}