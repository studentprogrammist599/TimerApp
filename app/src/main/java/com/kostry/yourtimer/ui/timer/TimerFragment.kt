package com.kostry.yourtimer.ui.timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.kostry.yourtimer.databinding.FragmentTimerBinding
import com.kostry.yourtimer.di.provider.TimerSubcomponentProvider
import com.kostry.yourtimer.service.TimerService
import com.kostry.yourtimer.ui.base.BaseFragment
import com.kostry.yourtimer.util.ViewModelFactory
import com.kostry.yourtimer.util.mapStringFormatTimeToMillis
import com.kostry.yourtimer.util.millisToStringFormat
import javax.inject.Inject

class TimerFragment : BaseFragment<FragmentTimerBinding>() {

    private val timerBroadcastReceiver: BroadcastReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val timeMillis: Long =
                    intent?.extras?.getLong(TimerService.INTENT_EXTRA_KEY_SERVICE_TIMER) ?: 0
                binding.timerFragmentTimerTextView.text = timeMillis.millisToStringFormat()
            }
        }
    }

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
        initBroadcastReceiver()
        initButton()
    }

    override fun onDestroy() {
        requireContext().unregisterReceiver(timerBroadcastReceiver)
        super.onDestroy()
    }

    private fun initBroadcastReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(TimerService.INTENT_FILTER_SERVICE_TIMER_SEND_BROADCAST)
        requireContext().registerReceiver(timerBroadcastReceiver, intentFilter)
    }

    private fun sendBroadcastToTimerService(command: Int, timeMillis: Long) {
        val intent = Intent(TimerService.INTENT_FILTER_FRAGMENT_TIMER_SEND_BROADCAST)
        intent.putExtra(TimerService.INTENT_EXTRA_KEY_FRAGMENT_TIMER_COMMAND, command)
        intent.putExtra(TimerService.INTENT_EXTRA_KEY_FRAGMENT_TIMER_MILLIS, timeMillis)
        requireActivity().sendBroadcast(intent)
    }

    private fun initButton() {
        binding.timerFragmentButtonStart.setOnClickListener {
            sendBroadcastToTimerService(TimerService.TIMER_START,
                binding.timerFragmentTimerTextView.text.toString().mapStringFormatTimeToMillis())
        }
        binding.timerFragmentButtonPause.setOnClickListener {
            sendBroadcastToTimerService(TimerService.TIMER_PAUSE,
                binding.timerFragmentTimerTextView.text.toString().mapStringFormatTimeToMillis())
        }
    }
}