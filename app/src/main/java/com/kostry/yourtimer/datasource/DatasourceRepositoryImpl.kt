package com.kostry.yourtimer.datasource

import android.util.Log
import com.kostry.yourtimer.datasource.database.AppDatabase
import com.kostry.yourtimer.datasource.models.PresetModel
import com.kostry.yourtimer.datasource.models.TimeCardModel
import com.kostry.yourtimer.util.getTimeCardEntities
import com.kostry.yourtimer.util.toPresetEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatasourceRepositoryImpl(
    private val appDatabase: AppDatabase,
) : DatasourceRepository {
    override suspend fun savePreset(presetModel: PresetModel) {
        withContext(Dispatchers.IO) {
            val ownerId = appDatabase.presetDao.insert(presetModel.toPresetEntity())
            appDatabase.timeCardDao.insertAll(presetModel.getTimeCardEntities(ownerId))
        }
    }

    override suspend fun deletePreset() {
        Log.d("TEST_TAG", "TEST")
    }

    override suspend fun getAllPresets(): List<PresetModel> {
        return withContext(Dispatchers.IO) {
            appDatabase.presetDao.getAll().map { presetEntity ->
                PresetModel(
                    id = presetEntity.id,
                    name = presetEntity.name,
                    timeCards = appDatabase.timeCardDao.getByOwnerId(presetEntity.id)
                        .map { cardEntity ->
                            TimeCardModel(
                                id = cardEntity.id,
                                enqueue = cardEntity.enqueue,
                                name = cardEntity.name,
                                reps = cardEntity.reps,
                                hours = cardEntity.hours,
                                minutes = cardEntity.minutes,
                                seconds = cardEntity.seconds,
                            )
                        }
                )
            }
        }
    }
}

