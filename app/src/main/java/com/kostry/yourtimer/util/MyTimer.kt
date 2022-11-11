package com.kostry.yourtimer.util

import com.kostry.yourtimer.datasource.models.PresetModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MyTimer {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var cardList: MutableList<Card> = mutableListOf()
    private val _timerState = MutableStateFlow<TimerState>(TimerState.Stopped)
    val timerState = _timerState.asStateFlow()

    fun runTimer(preset: PresetModel) {
        cardList = preset.toListTimeCard()
        startTimer()
    }

    fun restartTimer() {
        startTimer()
    }

    fun stopTimer() {
        _timerState.value = TimerState.Stopped
        coroutineScope.coroutineContext.cancelChildren()
    }

    fun pauseTimer() {
        _timerState.value = TimerState.Paused(getActualReps(), getActualTimeSeconds())
        coroutineScope.coroutineContext.cancelChildren()
    }

    private fun startTimer() {
        coroutineScope.launch {
            _timerState.value = TimerState.Running(getActualReps(), getActualTimeSeconds())
            while (_timerState.value is TimerState.Running) {
                val startTimeSecond = getActualTimeSeconds()
                for (reps in getActualReps() downTo 0) {
                    for (timeSeconds in getActualTimeSeconds() downTo 0) {
                        _timerState.value = TimerState.Running(reps, timeSeconds)
                        checkActualData(reps, timeSeconds, startTimeSecond)
                        delay(TIMER_INTERVAL)
                    }
                }
            }
        }
    }

    private fun checkActualData(reps: Int, actualTime: Long, startTime: Long) {
        if (reps == 0 && actualTime == 0L){
            stopTimer()
        }
        if (reps == 1 && actualTime ==0L){
            if(cardList.size == 0){
                stopTimer()
            }else{
                cardList.removeAt(0)
                coroutineScope.coroutineContext.cancelChildren()
                startTimer()
            }
        }
        if (actualTime == 0L && reps > 1){
            setActualData(reps, startTime)
        }
    }

    private fun setActualData(repsCount: Int, timeSeconds: Long) {
        with(cardList.first()){
            reps = repsCount
            hours = timeSeconds.fromSecondsGetHours().toInt()
            minutes = timeSeconds.fromSecondsGetMinutes().toInt()
            seconds = timeSeconds.fromSecondsGetSeconds().toInt()
        }
    }

    private fun getActualReps(): Int {
        return if (cardList.isNotEmpty()){
            cardList.first().reps
        } else {
            0
        }
    }

    private fun getActualTimeSeconds(): Long {
        return if (cardList.isNotEmpty()){
            cardList.first().let { timeCard ->
                mapTimeToSeconds(
                    hour = timeCard.hours,
                    minutes = timeCard.minutes,
                    seconds = timeCard.seconds
                )
            }
        } else {
            0
        }

    }

    companion object {
        private const val TIMER_INTERVAL: Long = 1000L
    }
}

fun PresetModel.toListTimeCard(): MutableList<Card> {
    return this.timeCards.map { timeCardModel ->
        Card(
            reps = timeCardModel.reps ?: 0,
            hours = timeCardModel.hours ?: 0,
            minutes = timeCardModel.minutes ?: 0,
            seconds = timeCardModel.seconds ?: 0,
        )
    }.toMutableList()
}

data class Card(
    var reps: Int,
    var hours: Int,
    var minutes: Int,
    var seconds: Int,
)