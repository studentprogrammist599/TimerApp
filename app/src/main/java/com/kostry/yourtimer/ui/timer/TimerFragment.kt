package com.kostry.yourtimer.ui.timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kostry.yourtimer.R
import com.kostry.yourtimer.databinding.FragmentTimerBinding
import com.kostry.yourtimer.di.provider.TimerSubcomponentProvider
import com.kostry.yourtimer.service.TimerService
import com.kostry.yourtimer.ui.base.BaseFragment
import com.kostry.yourtimer.util.TimerState
import com.kostry.yourtimer.util.ViewModelFactory
import com.kostry.yourtimer.util.mapStringFormatTimeToMillis
import com.kostry.yourtimer.util.millisToStringFormat
import com.kostry.yourtimer.util.sharedpref.SharedPrefsRepository
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class TimerFragment : BaseFragment<FragmentTimerBinding>() {

    private val args by navArgs<TimerFragmentArgs>()
    private val timerBroadcastReceiver: BroadcastReceiver by lazy {
        object :BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                val time: Long = intent?.extras?.getLong(TimerService.INTENT_KEY_TIMER) ?: 0
                Log.d("SERVICE_TAG", "BroadcastReceiver Message $time")
            }
        }
    }

    @Inject
    lateinit var sharedPrefsRepository: SharedPrefsRepository

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
        initBroadcastReceiver()
        initButton()
    }

    private fun initBroadcastReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(TimerService.INTENT_FILTER_TIMER)
        requireContext().registerReceiver(timerBroadcastReceiver, intentFilter)
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
                is TimerState.Finished -> {
                    navigationBackStack()
                }
                else -> {}
            }
        }
    }

    private fun navigationBackStack() {
        findNavController().popBackStack()
    }

    private fun initTimerState() {
        lifecycleScope.launchWhenCreated {
            viewModel.stateFlow
                .collectLatest {
                    when (it) {
                        is TimerState.Running -> {
                            setTextOnButton(getString(R.string.pause))
                            setTimeOnView(it.millis.millisToStringFormat())
                        }
                        is TimerState.Paused -> {
                            setTextOnButton(getString(R.string.start))
                            setTimeOnView(it.millis.millisToStringFormat())
                        }
                        is TimerState.Finished -> {
                            setTextOnButton(getString(R.string.back))
                            setTimeOnView(getString(R.string.finished))
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

    override fun onDestroy() {
        requireContext().unregisterReceiver(timerBroadcastReceiver)
        super.onDestroy()
    }
}