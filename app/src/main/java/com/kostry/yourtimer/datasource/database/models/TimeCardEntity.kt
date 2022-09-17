package com.kostry.yourtimer.datasource.database.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = PresetEntity::class,
            parentColumns = ["id"],
            childColumns = ["ownerId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TimeCardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val ownerId: Int,
    val name: String,
    val reps: Int,
    val hours: Int,
    val minutes: Int,
    val seconds: Int,
)
