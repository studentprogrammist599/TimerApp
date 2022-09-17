package com.kostry.yourtimer.datasource.database.dao

import androidx.room.*
import com.kostry.yourtimer.datasource.database.models.TimeCardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TimeCardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(timeCardEntity: List<TimeCardEntity>)

    @Query("SELECT * FROM TimeCardEntity WHERE ownerId = :ownerId")
    fun getByOwnerId(ownerId: Int): Flow<List<TimeCardEntity>>
}