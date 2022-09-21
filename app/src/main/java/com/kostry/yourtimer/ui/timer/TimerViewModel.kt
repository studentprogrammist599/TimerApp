package com.kostry.yourtimer.ui.timer

import com.kostry.yourtimer.datasource.models.PresetModel
import com.kostry.yourtimer.di.provider.TimerSubcomponentProvider
import com.kostry.yourtimer.ui.base.BaseViewModel
import com.kostry.yourtimer.util.MyTimer
import com.kostry.yourtimer.util.TimerState
import com.kostry.yourtimer.util.mapTimeToMillis
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

    fun runTimer(preset: PresetModel) {
        myTimer.runTimer(preset)
    }

    fun restartTimer() {
        myTimer.restartTimer()
    }

    fun pauseTimer() {
        myTimer.pauseTimer()
    }

    fun stopTimer() {
        myTimer.stopTimer()
    }

    fun getStartTime(preset: PresetModel?): Long {
        var totalTime = 0L
        preset?.timeCards?.forEach {
            val time =
                mapTimeToMillis(it.hours ?: 0, it.minutes ?: 0, it.seconds ?: 0)
            totalTime += time
        }
        return totalTime
    }

    override fun onCleared() {
        timerSubcomponentProvider.destroyTimerSubcomponent()
        super.onCleared()
    }
}