package com.kostry.yourtimer.di.provider

import com.kostry.yourtimer.di.component.HomeSubcomponent

interface HomeSubcomponentProvider {

    fun initHomeSubcomponent(): HomeSubcomponent
    fun destroyHomeSubcomponent()
}