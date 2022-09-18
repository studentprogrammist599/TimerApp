package com.kostry.yourtimer.util

import com.kostry.yourtimer.datasource.database.models.PresetEntity
import com.kostry.yourtimer.datasource.database.models.TimeCardEntity
import com.kostry.yourtimer.datasource.models.PresetModel

fun PresetModel.toPresetEntity() = PresetEntity(
    id = this.id,
    name = this.name
)

fun PresetModel.getTimeCardEntities(ownerId: Long) = this.timeCards.map {
    TimeCardEntity(
        id = it.id,
        ownerId = ownerId,
        enqueue = it.enqueue,
        name = it.name.orEmpty(),
        reps = it.reps ?: 0,
        hours = it.hours ?: 0,
        minutes = it.minutes ?: 0,
        seconds = it.seconds ?: 0,
    )
}