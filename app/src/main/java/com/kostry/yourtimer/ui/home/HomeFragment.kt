package com.kostry.yourtimer.ui.home

import android.content.Context
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kostry.yourtimer.R
import com.kostry.yourtimer.databinding.FragmentHomeBinding
import com.kostry.yourtimer.di.provider.HomeSubcomponentProvider
import com.kostry.yourtimer.ui.base.BaseFragment
import com.kostry.yourtimer.ui.timer.TimerFragment
import com.kostry.yourtimer.util.*
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
    }

    private val parentAdapter by lazy {
        HomeParentAdapter()
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
        initTimePicker()
        initPresetObserver()
        binding.homeFragmentRecycler.adapter = parentAdapter
        lifecycleScope.launchWhenCreated {
            viewModel.presets.collectLatest {
                parentAdapter.submitList(it)
            }
        }
    }

    private fun initPresetObserver() {
        lifecycleScope.launchWhenCreated {
            viewModel.presets.collectLatest { presetList ->
                Log.d("TEST_TAG", " preset size: ${presetList.size}")
            }
        }
    }

    private fun initTimePicker() {
        with(binding.homeFragmentQuickStartTimerView) {
            repsPicker.maxValue = TIMER_HOUR_PICKER_MAX_VALUE
            repsPicker.minValue = TIMER_HOUR_PICKER_MIN_VALUE
            hoursPicker.maxValue = TIMER_HOUR_PICKER_MAX_VALUE
            hoursPicker.minValue = TIMER_HOUR_PICKER_MIN_VALUE
            minutesPicker.maxValue = TIMER_MINUTE_SECOND_PICKER_MAX_VALUE
            minutesPicker.minValue = TIMER_MINUTE_SECOND_PICKER_MIN_VALUE
            secondsPicker.maxValue = TIMER_MINUTE_SECOND_PICKER_MAX_VALUE
            secondsPicker.minValue = TIMER_MINUTE_SECOND_PICKER_MIN_VALUE
            val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            hoursPicker.setOnValueChangedListener { _, _, _ ->
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        TIMER_PICKER_VIBRATE_TIME,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            }
            repsPicker.setOnValueChangedListener { _, _, _ ->
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        TIMER_PICKER_VIBRATE_TIME,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            }
            minutesPicker.setOnValueChangedListener { _, _, _ ->
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        TIMER_PICKER_VIBRATE_TIME,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            }
            secondsPicker.setOnValueChangedListener { _, _, _ ->
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        TIMER_PICKER_VIBRATE_TIME,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            }
        }
    }

    private fun initNavigationToTimerFragment() {
        binding.homeFragmentQuickStartButton.setOnClickListener {
            val seconds = binding.homeFragmentQuickStartTimerView.secondsPicker.value
            val minutes = binding.homeFragmentQuickStartTimerView.minutesPicker.value
            val hours = binding.homeFragmentQuickStartTimerView.hoursPicker.value
            val reps = binding.homeFragmentQuickStartTimerView.repsPicker.value
            val millis: Long = mapTimeToMillis(hours, minutes, seconds)
            if (millis != 0L && reps != 0) {
                findNavController().navigate(
                    R.id.action_homeFragment_to_timerFragment,
                    bundleOf(
                        TimerFragment.TIMER_FRAGMENT_TIME_ARGS_KEY to millis,
                        TimerFragment.TIMER_FRAGMENT_ROUND_ARGS_KEY to reps
                    )
                )
            }
        }
    }
}