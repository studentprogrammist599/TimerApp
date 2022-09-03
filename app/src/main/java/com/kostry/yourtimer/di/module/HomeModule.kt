package com.kostry.yourtimer.di.module

import android.app.Application
import androidx.lifecycle.ViewModel
import com.kostry.yourtimer.di.annotation.HomeScope
import com.kostry.yourtimer.di.annotation.ViewModelKey
import com.kostry.yourtimer.ui.home.HomeSubcomponentProvider
import com.kostry.yourtimer.ui.home.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
interface HomeModule {

    @HomeScope
    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    fun bindHomeViewModel(vm: HomeViewModel): ViewModel

    companion object{

        @HomeScope
        @Provides
        fun provideHomeSubcomponentProvider(
            application: Application
        ): HomeSubcomponentProvider{
            return (application as HomeSubcomponentProvider)
        }
    }
}