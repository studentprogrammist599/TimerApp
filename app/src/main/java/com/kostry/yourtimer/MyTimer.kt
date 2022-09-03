package com.kostry.yourtimer

import android.os.CountDownTimer

class MyTimer(
    millis: Long,
    interval: Long
): CountDownTimer(millis, interval) {
    override fun onTick(millisUntilFinished: Long) {
        TODO("Not yet implemented")
    }

    override fun onFinish() {
        TODO("Not yet implemented")
    }
}