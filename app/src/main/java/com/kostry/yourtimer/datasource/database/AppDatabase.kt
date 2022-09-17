package com.kostry.yourtimer.datasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kostry.yourtimer.datasource.database.dao.PresetDao
import com.kostry.yourtimer.datasource.database.dao.TimeCardDao
import com.kostry.yourtimer.datasource.database.models.PresetEntity
import com.kostry.yourtimer.datasource.database.models.TimeCardEntity

@Database(
    entities = [
        PresetEntity::class,
        TimeCardEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract val presetDao: PresetDao
    abstract val timeCardDao: TimeCardDao
}