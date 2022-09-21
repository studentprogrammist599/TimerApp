package com.kostry.yourtimer.datasource.models

import java.io.Serializable

data class PresetModel(
    val id: Int = 0,
    val name: String,
    val timeCards: List<TimeCardModel>
): Serializable
