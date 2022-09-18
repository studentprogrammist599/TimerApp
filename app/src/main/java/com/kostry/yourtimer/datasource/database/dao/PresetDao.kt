package com.kostry.yourtimer.datasource.database.dao

import androidx.room.*
import com.kostry.yourtimer.datasource.database.models.PresetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PresetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(presetEntity: PresetEntity): Long

    @Delete
    suspend fun delete(presetEntity: PresetEntity)

    @Query("SELECT * FROM PresetEntity")
    suspend fun getAll(): List<PresetEntity>
}