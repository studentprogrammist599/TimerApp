package com.kostry.yourtimer.util

import android.os.CountDownTimer
import com.kostry.yourtimer.datasource.models.PresetModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MyTimer {

    private var timer: CountDownTimer? = null
    private var cardList: MutableList<Card> = mutableListOf()
    private val _timerState = MutableStateFlow<TimerState>(TimerState.Stopped)
    val timerState = _timerState.asStateFlow()

    fun runTimer(preset: PresetModel) {
        cardList = toListTimeCard(preset)
        startTimer()
    }

    fun restartTimer() {
        startTimer()
    }

    fun stopTimer() {
        _timerState.value = TimerState.Stopped
        timer?.cancel()
    }

    fun pauseTimer() {
        _timerState.value = TimerState.Paused(
            reps = getActualReps(),
            millis = getActualTimeSeconds(),
            cardName = getActualCardName()
        )
        timer?.cancel()
    }

    private fun startTimer() {
        val startTimeSecond = getActualTimeSeconds()
        timer = object : CountDownTimer(getActualTimeSeconds(), TIMER_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                _timerState.value = TimerState.Running(
                    reps = getActualReps(),
                    millis = millisUntilFinished,
                    cardName = getActualCardName()
                )
                setActualData(getActualReps(), millisUntilFinished)
            }

            override fun onFinish() {
                checkActualData(getActualReps(), startTimeSecond)
            }
        }.start()
    }

    private fun checkActualData(reps: Int, startTime: Long) {
        if (cardList.isEmpty()) {
            stopTimer()
        } else {
            if (reps > 1) {
                timer?.cancel()
                setActualData(reps - 1, startTime)
                startTimer()
            } else {
                timer?.cancel()
                cardList.removeAt(0)
                startTimer()
            }
        }
    }

    private fun setActualData(repsCount: Int, timeMillis: Long) {
        with(cardList.first()) {
            reps = repsCount
            hours = timeMillis.fromMillisGetHours().toInt()
            minutes = timeMillis.fromMillisGetMinutes().toInt()
            seconds = timeMillis.fromMillisGetSeconds().toInt()
        }
    }

    private fun getActualReps(): Int {
        return if (cardList.isNotEmpty()) {
            cardList.first().reps
        } else {
            0
        }
    }

    private fun getActualTimeSeconds(): Long {
        return if (cardList.isNotEmpty()) {
            cardList.first().let { timeCard ->
                mapTimeToMillis(
                    hour = timeCard.hours,
                    minutes = timeCard.minutes,
                    seconds = timeCard.seconds
                )
            }
        } else {
            0
        }
    }

    private fun getActualCardName(): String {
        return if (cardList.isNotEmpty()) {
            cardList.first().cardName
        } else {
            ""
        }
    }

    private fun toListTimeCard(presetModel: PresetModel): MutableList<Card> {
        return presetModel.timeCards.map { timeCardModel ->
            Card(
                cardName = timeCardModel.name.orEmpty(),
                reps = timeCardModel.reps ?: 0,
                hours = timeCardModel.hours ?: 0,
                minutes = timeCardModel.minutes ?: 0,
                seconds = timeCardModel.seconds ?: 0,
            )
        }.toMutableList()
    }

    companion object {
        private const val TIMER_INTERVAL: Long = 1000L
    }
}

private data class Card(
    val cardName: String,
    var reps: Int,
    var hours: Int,
    var minutes: Int,
    var seconds: Int,
)