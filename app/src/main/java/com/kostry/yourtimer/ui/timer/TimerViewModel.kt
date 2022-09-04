package com.kostry.yourtimer.ui.timer

import android.os.CountDownTimer
import com.kostry.yourtimer.ui.base.BaseViewModel
import com.kostry.yourtimer.util.TimerState
import javax.inject.Inject

class TimerViewModel @Inject constructor(
    private val timerSubcomponentProvider: TimerSubcomponentProvider,
) : BaseViewModel() {

    private var timer: CountDownTimer? = null

    fun startTimer(millis: Long) {
        if (timer == null){
            timer = object : CountDownTimer(millis, 1000){
                override fun onTick(millisUntilFinished: Long) {
                    mutableStateFlow.value = TimerState.Running(millisUntilFinished)
                }
                override fun onFinish() {}
            }.start()
        }
    }

    fun pauseTimer(millis: Long){
        mutableStateFlow.value = TimerState.Paused(millis)
        timer?.cancel()
        timer = null
    }

    override fun onCleared() {
        timerSubcomponentProvider.destroyTimerSubcomponent()
        super.onCleared()
    }
}