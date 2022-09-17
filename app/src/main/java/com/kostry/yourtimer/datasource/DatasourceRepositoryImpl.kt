package com.kostry.yourtimer.datasource

import android.util.Log
import com.kostry.yourtimer.datasource.database.AppDatabase
import com.kostry.yourtimer.datasource.models.PresetModel
import com.kostry.yourtimer.util.getTimeCardEntities
import com.kostry.yourtimer.util.toPresetEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class DatasourceRepositoryImpl(
    private val appDatabase: AppDatabase,
) : DatasourceRepository {
    override suspend fun savePreset(presetModel: PresetModel) {
        withContext(Dispatchers.IO) {
            appDatabase.presetDao.insert(presetModel.toPresetEntity())
            appDatabase.timeCardDao.insertAll(presetModel.getTimeCardEntities())
        }
    }

    override suspend fun deletePreset() {
        Log.d("TEST_TAG", "TEST")
    }

    override fun getAllPresets(): Flow<List<PresetModel>> {
        TODO()
    }
}