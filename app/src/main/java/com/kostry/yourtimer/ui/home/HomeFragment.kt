package com.kostry.yourtimer.ui.home

import android.view.ViewGroup
import com.kostry.yourtimer.databinding.FragmentHomeBinding
import com.kostry.yourtimer.ui.base.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override fun getViewBinding(container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater, container, false)
    }
}