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
import com.kostry.yourtimer.util.TimerState
import com.kostry.yourtimer.util.ViewModelFactory
import com.kostry.yourtimer.util.mapStringFormatTimeToMillis
import com.kostry.yourtimer.util.millisToStringFormat
import kotlinx.coroutines.flow.collectLatest
import java.lang.RuntimeException
import javax.inject.Inject

class TimerFragment : BaseFragment<FragmentTimerBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: TimerViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[TimerViewModel::class.java]
    }

    private val args: Long by lazy {
        arguments?.getLong(TIMER_FRAGMENT_ARGS_KEY) ?: throw RuntimeException("args is null")
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
        viewModel.startTimer(args)
        initViewState()
        initButtonClickListener()
    }

    override fun onStop() {
        super.onStop()
        mainActivityCallback.startTimerService()
    }

    private fun initViewState() {
        lifecycleScope.launchWhenCreated {
            viewModel.timerState.collectLatest { state ->
                when (state) {
                    is TimerState.Running -> {
                        binding.timerFragmentTextView.text = state.millis.millisToStringFormat()
                        binding.timerFragmentButton.text = getString(R.string.pause)
                    }
                    is TimerState.Paused -> {
                        binding.timerFragmentTextView.text = state.millis.millisToStringFormat()
                        binding.timerFragmentButton.text = getString(R.string.start)
                    }
                    is TimerState.Stopped -> {
                        binding.timerFragmentTextView.text = getString(R.string.finished)
                        binding.timerFragmentButton.text = getString(R.string.back)
                    }
                }
            }
        }
    }

    private fun initButtonClickListener() {
        binding.timerFragmentButton.setOnClickListener {
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
    }
    companion object {
        const val TIMER_FRAGMENT_ARGS_KEY = "TIMER_FRAGMENT_ARGS_KEY"
    }
}