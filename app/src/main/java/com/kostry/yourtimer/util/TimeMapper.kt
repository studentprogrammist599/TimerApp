package com.kostry.yourtimer.util

import java.util.concurrent.TimeUnit

fun Long.secondsToStringFormat() = String.format("%02d:%02d:%02d",
    TimeUnit.SECONDS.toHours(this),
    TimeUnit.SECONDS.toMinutes(this) - TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(
        this)),
    TimeUnit.SECONDS.toSeconds(this) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(
        this)))

fun Long.fromSecondsGetHours() = TimeUnit.SECONDS.toHours(this)

fun Long.fromSecondsGetMinutes() =
    TimeUnit.SECONDS.toMinutes(this) - TimeUnit.HOURS.toMinutes(
        TimeUnit.SECONDS.toHours(this)
    )

fun Long.fromSecondsGetSeconds() =
    TimeUnit.SECONDS.toSeconds(this) - TimeUnit.MINUTES.toSeconds(
        TimeUnit.SECONDS.toMinutes(this)
    )

fun mapTimeToSeconds(hour: Int, minutes: Int, seconds: Int): Long {
    return (seconds + (minutes * 60) + (hour * 60 * 60)).toLong()
}

fun Int.intSubTimeStringFormat():String {
    return if (this >= 10){
        this.toString()
    }else{
        "0$this"
    }
}