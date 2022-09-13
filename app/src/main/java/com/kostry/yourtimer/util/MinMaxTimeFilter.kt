package com.kostry.yourtimer.util

import android.text.InputFilter
import android.text.Spanned

class MinMaxTimeFilter() : InputFilter {
    private val intMin: Int = 0
    private val intMax: Int = 59

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dStart: Int,
        dEnd: Int,
    ): CharSequence? {
        val input = (dest.toString() + source.toString()).toIntOrNull()
        if (input in intMin..intMax) {
            return null
        }
        return ""
    }
}