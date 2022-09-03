package com.kostry.yourtimer.ui.home

import com.kostry.yourtimer.di.component.HomeSubcomponent

interface HomeSubcomponentProvider {

    fun initHomeSubcomponent(): HomeSubcomponent
    fun destroyHomeSubcomponent()
}