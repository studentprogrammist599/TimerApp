package com.kostry.yourtimer.util

import android.text.InputFilter
import android.text.Spanned

class MinMaxTimeFilter() : InputFilter {

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
            if (input in INT_MIN..INT_MAX) {
                return null
            }
            return ""
        }catch (e: Exception){
            return ""
        }
    }

    companion object{
        private const val INT_MIN: Int = 0
        private const val INT_MAX: Int = 59
    }
}