package com.kostry.yourtimer.di.component

import com.kostry.yourtimer.di.annotation.PresetScope
import com.kostry.yourtimer.di.module.PresetModule
import com.kostry.yourtimer.ui.preset.PresetFragment
import dagger.Subcomponent

@PresetScope
@Subcomponent(modules = [PresetModule::class])
interface PresetSubcomponent {

    fun inject(fragment: PresetFragment)
}