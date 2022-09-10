package com.kostry.yourtimer.ui.home

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kostry.yourtimer.R
import com.kostry.yourtimer.databinding.FragmentHomeBinding
import com.kostry.yourtimer.di.provider.HomeSubcomponentProvider
import com.kostry.yourtimer.ui.base.BaseFragment
import com.kostry.yourtimer.ui.timer.TimerFragment
import com.kostry.yourtimer.util.ViewModelFactory
import com.kostry.yourtimer.util.mapTimeToMillis
import javax.inject.Inject

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        (requireActivity().application as HomeSubcomponentProvider)
            .initHomeSubcomponent()
            .inject(this)
        super.onAttach(context)
    }

    override fun getViewBinding(container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNavigationToTimerFragment()
    }

    private fun initNavigationToTimerFragment() {
        binding.homeFragmentQuickStartButton.setOnClickListener {
            val millis: Long = mapTimeToMillis(
                hour = binding.homeFragmentQuickStartHourEditText.text
                    .toString().toIntOrNull() ?: 0,
                minutes = binding.homeFragmentQuickStartMinutesEditText.text
                    .toString().toIntOrNull() ?: 0,
                seconds = binding.homeFragmentQuickStartSecondsEditText.text
                    .toString().toIntOrNull() ?: 0,
            )
            if (millis != 0L) {
                findNavController().navigate(
                    R.id.action_homeFragment_to_timerFragment,
                    bundleOf(TimerFragment.TIMER_FRAGMENT_ARGS_KEY to millis)
                )
            }
        }
    }
}