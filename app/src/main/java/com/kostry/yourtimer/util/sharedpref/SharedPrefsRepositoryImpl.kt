package com.kostry.yourtimer.util.sharedpref

import android.content.SharedPreferences

class SharedPrefsRepositoryImpl(
    private val sharedPreference: SharedPreferences
) : SharedPrefsRepository {
    private val IS_ACTIVE = "IS_ACTIVE"

    override var timerServiceIsActive: Boolean?
        get() {
            return sharedPreference.getBoolean(IS_ACTIVE, false)
        }
        set(value) {
            with(sharedPreference.edit()) {
                if (value == null) {
                    remove(IS_ACTIVE)
                }
                else {
                    putBoolean(IS_ACTIVE, value)
                }
                apply()
            }
        }
}
