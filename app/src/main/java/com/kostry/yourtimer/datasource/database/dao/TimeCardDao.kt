package com.kostry.yourtimer.datasource.database.dao

import androidx.room.*
import com.kostry.yourtimer.datasource.database.models.TimeCardEntity

@Dao
interface TimeCardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(timeCardEntity: List<TimeCardEntity>)

    @Query("SELECT * FROM TimeCardEntity WHERE ownerId = :ownerId")
    suspend fun getByOwnerId(ownerId: Int): List<TimeCardEntity>
}