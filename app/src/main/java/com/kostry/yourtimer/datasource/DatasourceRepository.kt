package com.kostry.yourtimer.datasource

import com.kostry.yourtimer.datasource.models.PresetModel
import kotlinx.coroutines.flow.Flow

interface DatasourceRepository {

    suspend fun savePreset(presetModel: PresetModel)
    suspend fun deletePreset(presetModel: PresetModel)
    suspend fun getAllPresets(): List<PresetModel>
}