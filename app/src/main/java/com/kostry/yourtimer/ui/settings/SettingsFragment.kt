package com.kostry.yourtimer.ui.settings

import android.view.ViewGroup
import com.kostry.yourtimer.databinding.FragmentSettingsBinding
import com.kostry.yourtimer.ui.base.BaseFragment

class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    override fun getViewBinding(container: ViewGroup?) =
        FragmentSettingsBinding.inflate(layoutInflater, container, false)

}