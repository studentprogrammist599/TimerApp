package com.kostry.yourtimer.datasource.models

data class TimeCardModel(
    val id: Int,
    var enqueue: Int = 0,
    val name: String? = null,
    val reps: Int? = null,
    val hours: Int? = null,
    val minutes: Int? = null,
    val seconds: Int? = null,
)
