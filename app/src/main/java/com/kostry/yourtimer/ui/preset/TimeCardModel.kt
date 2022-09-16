package com.kostry.yourtimer.ui.preset

data class TimeCardModel(
    val id: Int,
    val name: String? = null,
    val reps: Int? = null,
    val hours: Int? = null,
    val minutes: Int? = null,
    val seconds: Int? = null,
)
