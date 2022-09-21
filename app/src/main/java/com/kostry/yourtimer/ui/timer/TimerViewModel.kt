package com.kostry.yourtimer.ui.timer

import com.kostry.yourtimer.datasource.models.PresetModel
import com.kostry.yourtimer.di.provider.TimerSubcomponentProvider
import com.kostry.yourtimer.ui.base.BaseViewModel
import com.kostry.yourtimer.util.MyTimer
import com.kostry.yourtimer.util.TimerState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class TimerViewModel @Inject constructor(
    private val timerSubcomponentProvider: TimerSubcomponentProvider,
    private val myTimer: MyTimer,
) : BaseViewModel() {

    val timerState: StateFlow<TimerState> = myTimer.timerState
        .stateIn(
            scope = baseViewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = TimerState.Stopped
        )

    fun runTimer(preset: PresetModel){

    }

    fun restartTimer() {
        myTimer.restartTimer()
    }

    fun pauseTimer() {
        myTimer.pauseTimer()
    }

    fun stopTimer(){
        myTimer.stopTimer()
    }

    fun getStartTime() = myTimer.getStartTime()

    override fun onCleared() {
        timerSubcomponentProvider.destroyTimerSubcomponent()
        super.onCleared()
    }
}