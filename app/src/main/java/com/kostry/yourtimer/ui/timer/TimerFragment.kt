package com.kostry.yourtimer.ui.timer

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kostry.yourtimer.R
import com.kostry.yourtimer.databinding.FragmentTimerBinding
import com.kostry.yourtimer.di.provider.TimerSubcomponentProvider
import com.kostry.yourtimer.ui.base.BaseFragment
import com.kostry.yourtimer.ui.mainactivity.MainActivityCallback
import com.kostry.yourtimer.util.*
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class TimerFragment : BaseFragment<FragmentTimerBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: TimerViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[TimerViewModel::class.java]
    }

    private val args by navArgs<TimerFragmentArgs>()

    private val mainActivityCallback by lazy {
        activity as MainActivityCallback
    }

    override fun onAttach(context: Context) {
        (requireActivity().application as TimerSubcomponentProvider)
            .initTimerSubcomponent()
            .inject(this)
        super.onAttach(context)
    }

    override fun getViewBinding(container: ViewGroup?) =
        FragmentTimerBinding.inflate(layoutInflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.timerState.value !is TimerState.Paused
            && savedInstanceState == null
            && args.preset != null
        ) {
            viewModel.runTimer(args.preset!!)
        }
        initTimePicker()
        initViewState()
        initButtonClickListener()
    }

    override fun onStart() {
        super.onStart()
        mainActivityCallback.stopTimerService()
    }

    override fun onStop() {
        super.onStop()
        startTimerService()
    }

    private fun startTimerService() {
        if (viewModel.timerState.value !is TimerState.Stopped) {
            mainActivityCallback.startTimerService()
        }
    }

    private fun initViewState() {
        lifecycleScope.launchWhenCreated {
            viewModel.timerState.collectLatest { state ->
                when (state) {
                    is TimerState.Running -> {
                        renderState(
                            reps = state.reps,
                            timeMillis = state.millis,
                            buttonStartPauseText = getString(R.string.pause),
                            buttonCancelVisibility = View.VISIBLE,
                            positiveButtonColor = requireContext().getColor(R.color.third_button_color),
                        )
                    }
                    is TimerState.Paused -> {
                        renderState(
                            reps = state.reps,
                            timeMillis = state.millis,
                            buttonStartPauseText = getString(R.string.start),
                            buttonCancelVisibility = View.VISIBLE,
                            positiveButtonColor = requireContext().getColor(R.color.primary_button_color),
                        )
                    }
                    is TimerState.Stopped -> {
                        renderState(
                            reps = 0,
                            timeMillis = 0,
                            buttonStartPauseText = getString(R.string.back),
                            buttonCancelVisibility = View.GONE,
                            positiveButtonColor = requireContext().getColor(R.color.primary_button_color),
                        )
                    }
                }
            }
        }
    }

    private fun renderState(
        reps: Int,
        timeMillis: Long,
        buttonStartPauseText: String,
        buttonCancelVisibility: Int,
        positiveButtonColor: Int,
    ) {
        setTime(reps, timeMillis)
        binding.timerFragmentPositiveButton.text = buttonStartPauseText
        binding.timerFragmentNegativeButton.visibility = buttonCancelVisibility
        binding.timerFragmentPositiveButton.setBackgroundColor(positiveButtonColor)
    }

    private fun initButtonClickListener() {
        binding.timerFragmentPositiveButton.setOnClickListener {
            when (viewModel.timerState.value) {
                is TimerState.Running -> {
                    viewModel.pauseTimer()
                }
                is TimerState.Paused -> {
                    viewModel.restartTimer()
                }
                is TimerState.Stopped -> {
                    findNavController().popBackStack()
                }
            }
        }
        binding.timerFragmentNegativeButton.setOnClickListener {
            viewModel.stopTimer()
            findNavController().popBackStack()
        }
    }

    private fun initTimePicker() {
        with(binding.timerFragmentTimerView) {
            repsPicker.maxValue = TIMER_HOUR_PICKER_MAX_VALUE
            repsPicker.minValue = TIMER_HOUR_PICKER_MIN_VALUE
            hoursPicker.maxValue = TIMER_HOUR_PICKER_MAX_VALUE
            hoursPicker.minValue = TIMER_HOUR_PICKER_MIN_VALUE
            minutesPicker.maxValue = TIMER_MINUTE_SECOND_PICKER_MAX_VALUE
            minutesPicker.minValue = TIMER_MINUTE_SECOND_PICKER_MIN_VALUE
            secondsPicker.maxValue = TIMER_MINUTE_SECOND_PICKER_MAX_VALUE
            secondsPicker.minValue = TIMER_MINUTE_SECOND_PICKER_MIN_VALUE
        }
    }

    private fun setTime(reps: Int, timeMillis: Long) {
        with(binding.timerFragmentTimerView) {
            repsPicker.value = reps
            hoursPicker.value = timeMillis.fromMillisGetHours().toInt()
            minutesPicker.value = timeMillis.fromMillisGetMinutes().toInt()
            secondsPicker.value = timeMillis.fromMillisGetSeconds().toInt()
        }
    }
}