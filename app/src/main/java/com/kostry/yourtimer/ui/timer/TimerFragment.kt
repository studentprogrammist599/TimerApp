package com.kostry.yourtimer.ui.timer

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.kostry.yourtimer.databinding.FragmentTimerBinding
import com.kostry.yourtimer.ui.base.BaseFragment
import com.kostry.yourtimer.util.TimerState
import com.kostry.yourtimer.util.ViewModelFactory
import com.kostry.yourtimer.util.mapStringFormatTimeToMillis
import com.kostry.yourtimer.util.millisToStringFormat
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class TimerFragment : BaseFragment<FragmentTimerBinding>() {

    private val args by navArgs<TimerFragmentArgs>()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: TimerViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[TimerViewModel::class.java]
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
        initTimerState()
        initButton()
    }

    private fun initButton() {
        binding.timerFragmentButton.setOnClickListener {
            when (viewModel.stateFlow.value) {
                is TimerState.Running -> {
                    viewModel.pauseTimer(
                        binding.timerFragmentTimerTextView.text.toString()
                            .mapStringFormatTimeToMillis()
                    )
                }
                is TimerState.Paused -> {
                    viewModel.startTimer(
                        binding.timerFragmentTimerTextView.text.toString()
                            .mapStringFormatTimeToMillis()
                    )
                }
                else -> {}
            }
        }
    }

    private fun initTimerState() {
        lifecycleScope.launchWhenCreated {
            viewModel.stateFlow
                .collectLatest {
                    when (it) {
                        is TimerState.Running -> {
                            setTextOnButton("pause")
                            setTimeOnView(it.millis.millisToStringFormat())
                        }
                        is TimerState.Paused -> {
                            setTextOnButton("start")
                            setTimeOnView(it.millis.millisToStringFormat())
                        }
                        else -> {}
                    }
                }
        }
    }

    private fun setTextOnButton(text: String) {
        binding.timerFragmentButton.text = text
    }

    private fun setTimeOnView(time: String) {
        binding.timerFragmentTimerTextView.text = time
    }

    override fun onStart() {
        super.onStart()
        when (viewModel.stateFlow.value) {
            is TimerState.NotAttached -> {
                viewModel.startTimer(args.millis)
            }
            else -> {}
        }
    }
}