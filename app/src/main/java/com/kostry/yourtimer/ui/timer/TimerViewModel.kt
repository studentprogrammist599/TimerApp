package com.kostry.yourtimer.ui.timer

import android.os.CountDownTimer
import com.kostry.yourtimer.ui.base.BaseViewModel
import javax.inject.Inject

class TimerViewModel @Inject constructor(
    private val timerSubcomponentProvider: TimerSubcomponentProvider,
) : BaseViewModel() {

    fun startTimer(millis: Long) {
        object : CountDownTimer(millis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                mutableStateFlow.value = millisUntilFinished
            }

            override fun onFinish() {
            }
        }.start()
    }

    override fun onCleared() {
        timerSubcomponentProvider.destroyTimerSubcomponent()
        super.onCleared()
    }
}