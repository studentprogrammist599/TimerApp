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
        try {
            val input = Integer.parseInt(dest.toString() + source.toString())
            if (input in intMin..intMax) {
                return null
            }
            return ""
        }catch (e: Exception){
            return ""
        }
    }
}