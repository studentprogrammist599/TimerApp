package com.kostry.yourtimer.util

import java.util.concurrent.TimeUnit

fun Long.millisToStringFormat() = String.format("%02d:%02d:%02d",
    TimeUnit.MILLISECONDS.toHours(this),
    TimeUnit.MILLISECONDS.toMinutes(this) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(
        this)),
    TimeUnit.MILLISECONDS.toSeconds(this) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(
        this)))

fun String.mapStringFormatTimeToMillis(): Long {
    return mapTimeToMillis(
        hour = this.substring(0..1).toInt(),
        minutes = this.substring(3..4).toInt(),
        seconds = this.substring(6..7).toInt(),
    )
}

fun mapTimeToMillis(hour: Int, minutes: Int, seconds: Int): Long {
    return ((seconds * 1000) + (minutes * 1000 * 60) + (hour * 1000 * 60 * 60)).toLong()
}

fun getPercentProgressTime(actualTime: Long, startTIme: Long): Int =
    (actualTime.toFloat() / startTIme.toFloat() * 100).toInt()