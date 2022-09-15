package com.kostry.yourtimer.di.provider

import com.kostry.yourtimer.di.component.PresetSubcomponent

interface PresetSubcomponentProvider {

    fun iniPresetSubcomponent(): PresetSubcomponent
    fun destroyPresetSubcomponent()
}