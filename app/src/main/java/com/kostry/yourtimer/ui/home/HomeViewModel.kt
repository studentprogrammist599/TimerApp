package com.kostry.yourtimer.ui.home

import com.kostry.yourtimer.di.provider.HomeSubcomponentProvider
import com.kostry.yourtimer.ui.base.BaseViewModel
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val homeSubcomponentProvider: HomeSubcomponentProvider
) : BaseViewModel() {

    override fun onCleared() {
        homeSubcomponentProvider.destroyHomeSubcomponent()
        super.onCleared()
    }
}