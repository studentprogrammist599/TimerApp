package com.kostry.yourtimer.util

import com.kostry.yourtimer.datasource.database.models.PresetEntity
import com.kostry.yourtimer.datasource.database.models.TimeCardEntity
import com.kostry.yourtimer.datasource.models.PresetModel
import com.kostry.yourtimer.datasource.models.TimeCardModel

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

fun PresetEntity.toPresetModel(cardEntityList: List<TimeCardEntity>): PresetModel = PresetModel(
    id = this.id,
    name = this.name,
    timeCards = cardEntityList.toListTimeCardModels()
)

fun List<TimeCardEntity>.toListTimeCardModels(): List<TimeCardModel> = this.map { timeCardEntity ->
    timeCardEntity.toTimeCardModel()
}

fun TimeCardEntity.toTimeCardModel(): TimeCardModel = TimeCardModel(
    id = this.id,
    enqueue = this.enqueue,
    name = this.name,
    reps = this.reps,
    hours = this.hours,
    minutes = this.minutes,
    seconds = this.seconds,
)