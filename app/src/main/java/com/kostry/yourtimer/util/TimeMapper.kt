package com.kostry.yourtimer.util

import java.util.concurrent.TimeUnit

fun Long.millisToStringFormat() = String.format(
    "%02d:%02d:%02d",
    TimeUnit.MILLISECONDS.toHours(this),
    TimeUnit.MILLISECONDS.toMinutes(this) - TimeUnit.HOURS.toMinutes(
        TimeUnit.MILLISECONDS.toHours(
            this
        )
    ),
    TimeUnit.MILLISECONDS.toSeconds(this) - TimeUnit.MINUTES.toSeconds(
        TimeUnit.MILLISECONDS.toMinutes(
            this
        )
    )
)

fun Long.fromMillisGetHours() = TimeUnit.MILLISECONDS.toHours(this)

fun Long.fromMillisGetMinutes() =
    TimeUnit.MILLISECONDS.toMinutes(this) - TimeUnit.HOURS.toMinutes(
        TimeUnit.MILLISECONDS.toHours(this)
    )

fun Long.fromMillisGetSeconds() =
    TimeUnit.MILLISECONDS.toSeconds(this) - TimeUnit.MINUTES.toSeconds(
        TimeUnit.MILLISECONDS.toMinutes(this)
    )

fun mapTimeToMillis(hour: Int, minutes: Int, seconds: Int): Long {
    return ((seconds * 1000) + (minutes * 1000 * 60) + (hour * 1000 * 60 * 60)).toLong()
}

fun Int.intSubTimeStringFormat(): String {
    return if (this >= 10) {
        this.toString()
    } else {
        "0$this"
    }
}