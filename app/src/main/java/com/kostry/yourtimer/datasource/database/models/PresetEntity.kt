package com.kostry.yourtimer.datasource.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PresetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String
)