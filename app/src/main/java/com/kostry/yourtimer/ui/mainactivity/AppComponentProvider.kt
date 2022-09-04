package com.kostry.yourtimer.ui.mainactivity

import com.kostry.yourtimer.di.component.AppComponent

interface AppComponentProvider {

    fun provideAppComponent(): AppComponent
}