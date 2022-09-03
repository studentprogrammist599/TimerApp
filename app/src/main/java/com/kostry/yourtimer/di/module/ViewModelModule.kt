package com.kostry.yourtimer.di.module

import androidx.lifecycle.ViewModelProvider
import com.kostry.yourtimer.util.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}