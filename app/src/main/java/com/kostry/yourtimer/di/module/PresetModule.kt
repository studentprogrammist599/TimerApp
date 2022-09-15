package com.kostry.yourtimer.di.module

import android.app.Application
import androidx.lifecycle.ViewModel
import com.kostry.yourtimer.di.annotation.PresetScope
import com.kostry.yourtimer.di.annotation.ViewModelKey
import com.kostry.yourtimer.di.provider.PresetSubcomponentProvider
import com.kostry.yourtimer.ui.preset.PresetViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
interface PresetModule {

    @PresetScope
    @Binds
    @IntoMap
    @ViewModelKey(PresetViewModel::class)
    fun bindPresetViewModel(vm: PresetViewModel): ViewModel

    companion object {
        @PresetScope
        @Provides
        fun providePresetSubcomponentProvider(
            application: Application,
        ): PresetSubcomponentProvider {
            return (application as PresetSubcomponentProvider)
        }
    }
}