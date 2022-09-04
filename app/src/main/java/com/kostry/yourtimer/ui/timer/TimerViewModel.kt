package com.kostry.yourtimer.ui.timer

import android.os.CountDownTimer
import com.kostry.yourtimer.di.provider.TimerSubcomponentProvider
import com.kostry.yourtimer.ui.base.BaseViewModel
import com.kostry.yourtimer.util.TimerState
import javax.inject.Inject

class TimerViewModel @Inject constructor(
    private val timerSubcomponentProvider: TimerSubcomponentProvider,
) : BaseViewModel() {

    fun startTimer(millis: Long) {
        if (timer == null) {
            timer = object : CountDownTimer(millis, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    mutableStateFlow.value = TimerState.Running(millisUntilFinished)
                }

                override fun onFinish() {
                    mutableStateFlow.value = TimerState.Finished
                }
            }.start()
        }
    }

    fun pauseTimer(millis: Long) {
        if (timer != null) {
            mutableStateFlow.value = TimerState.Paused(millis)
            timer?.cancel()
            timer = null
        }
    }

    override fun onCleared() {
        timerSubcomponentProvider.destroyTimerSubcomponent()
        super.onCleared()
    }
}