package com.kostry.yourtimer.ui.home

import com.kostry.yourtimer.datasource.DatasourceRepository
import com.kostry.yourtimer.datasource.models.PresetModel
import com.kostry.yourtimer.di.provider.HomeSubcomponentProvider
import com.kostry.yourtimer.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val homeSubcomponentProvider: HomeSubcomponentProvider,
    private val repository: DatasourceRepository
) : BaseViewModel() {

    private val _presets = MutableStateFlow<List<PresetModel>>(emptyList())
    val presets: StateFlow<List<PresetModel>> = _presets.asStateFlow()

    fun getPresets() {
        baseViewModelScope.launch {
            _presets.value = repository.getAllPresets()
        }
    }

    fun deletePreset(presetModel: PresetModel) {
        baseViewModelScope.launch {
            repository.deletePreset(presetModel)
            getPresets()
        }
    }

    override fun onCleared() {
        homeSubcomponentProvider.destroyHomeSubcomponent()
        super.onCleared()
    }
}