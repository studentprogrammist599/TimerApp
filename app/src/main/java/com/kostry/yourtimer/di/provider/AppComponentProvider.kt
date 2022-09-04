package com.kostry.yourtimer.di.provider

import com.kostry.yourtimer.di.component.AppComponent

interface AppComponentProvider {

    fun provideAppComponent(): AppComponent
}