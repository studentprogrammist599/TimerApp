package com.kostry.yourtimer.datasource.models

data class PresetModel(
    val id: Int = 0,
    val name: String,
    val timeCards: List<TimeCardModel>
)
