package com.kostry.yourtimer.ui.timer

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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

    private val args: Long by lazy {
        arguments?.getLong(TIMER_FRAGMENT_ARGS_KEY)
            ?: throw RuntimeException("TimerFragment args is null")
    }
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
        if (viewModel.timerState.value !is TimerState.Paused && savedInstanceState == null) {
            viewModel.startTimer(args)
        }
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
                            time = state.millis.millisToStringFormat(),
                            buttonStartPauseText = getString(R.string.pause),
                            buttonCancelVisibility = View.VISIBLE,
                            setProgress = getPercentProgressTime(state.millis, args)
                        )
                    }
                    is TimerState.Paused -> {
                        renderState(
                            time = state.millis.millisToStringFormat(),
                            buttonStartPauseText = getString(R.string.start),
                            buttonCancelVisibility = View.VISIBLE,
                            setProgress = getPercentProgressTime(state.millis, args)
                        )
                    }
                    is TimerState.Stopped -> {
                        renderState(
                            time = getString(R.string.finished),
                            buttonStartPauseText = getString(R.string.back),
                            buttonCancelVisibility = View.GONE,
                            setProgress = 0
                        )
                    }
                }
            }
        }
    }

    private fun renderState(
        time: String,
        buttonStartPauseText: String,
        buttonCancelVisibility: Int,
        setProgress: Int
    ) {
        binding.timerFragmentTextView.text = time
        binding.timerFragmentButtonStartPause.text = buttonStartPauseText
        binding.timerFragmentButtonCancel.visibility = buttonCancelVisibility
        binding.timerFragmentProgressBar.setProgress(setProgress, true)
    }

    private fun initButtonClickListener() {
        binding.timerFragmentButtonStartPause.setOnClickListener {
            when (viewModel.timerState.value) {
                is TimerState.Running -> {
                    viewModel.pauseTimer(
                        binding.timerFragmentTextView.text.toString().mapStringFormatTimeToMillis()
                    )
                }
                is TimerState.Paused -> {
                    viewModel.startTimer(
                        binding.timerFragmentTextView.text.toString().mapStringFormatTimeToMillis()
                    )
                }
                is TimerState.Stopped -> {
                    findNavController().popBackStack()
                }
            }
        }
        binding.timerFragmentButtonCancel.setOnClickListener {
            viewModel.stopTimer()
            findNavController().popBackStack()
        }
    }

    companion object {
        const val TIMER_FRAGMENT_ARGS_KEY = "TIMER_FRAGMENT_ARGS_KEY"
    }
}