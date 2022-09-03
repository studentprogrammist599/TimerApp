package com.kostry.yourtimer.di.component

import com.kostry.yourtimer.di.annotation.HomeScope
import com.kostry.yourtimer.di.module.HomeModule
import com.kostry.yourtimer.ui.home.HomeFragment
import dagger.Subcomponent

@HomeScope
@Subcomponent(modules = [HomeModule::class])
interface HomeSubcomponent {

    fun inject(homeFragment: HomeFragment)
}