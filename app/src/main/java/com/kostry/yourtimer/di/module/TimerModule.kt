package com.kostry.yourtimer.di.module

import android.app.Application
import androidx.lifecycle.ViewModel
import com.kostry.yourtimer.di.annotation.TimerScope
import com.kostry.yourtimer.di.annotation.ViewModelKey
import com.kostry.yourtimer.di.provider.TimerSubcomponentProvider
import com.kostry.yourtimer.ui.timer.TimerViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
interface TimerModule {

    @TimerScope
    @Binds
    @IntoMap
    @ViewModelKey(TimerViewModel::class)
    fun bindTimerViewModel(vm: TimerViewModel): ViewModel

    companion object {

        @TimerScope
        @Provides
        fun provideTimerSubcomponentProvider(
            application: Application
        ): TimerSubcomponentProvider {
            return (application as TimerSubcomponentProvider)
        }
    }
}