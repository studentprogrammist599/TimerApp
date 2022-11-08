package com.kostry.yourtimer.datasource

import com.kostry.yourtimer.datasource.database.AppDatabase
import com.kostry.yourtimer.datasource.models.PresetModel
import com.kostry.yourtimer.util.getTimeCardEntities
import com.kostry.yourtimer.util.toPresetEntity
import com.kostry.yourtimer.util.toPresetModel
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

    override suspend fun deletePreset(presetModel: PresetModel) {
        withContext(Dispatchers.IO) {
            appDatabase.presetDao.delete(presetModel.toPresetEntity())
        }
    }

    override suspend fun getAllPresets(): List<PresetModel> {
        return withContext(Dispatchers.IO) {
            appDatabase.presetDao.getAll().map { presetEntity ->
                presetEntity.toPresetModel(appDatabase.timeCardDao.getByOwnerId(presetEntity.id))
            }
        }
    }
}

