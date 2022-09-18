package com.kostry.yourtimer.ui.home

import com.kostry.yourtimer.datasource.DatasourceRepository
import com.kostry.yourtimer.datasource.models.PresetModel
import com.kostry.yourtimer.di.provider.HomeSubcomponentProvider
import com.kostry.yourtimer.ui.base.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val homeSubcomponentProvider: HomeSubcomponentProvider,
    private val repository: DatasourceRepository
) : BaseViewModel() {

    val presets: StateFlow<List<PresetModel>> = repository.getAllPresets().stateIn(
        scope = baseViewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    override fun onCleared() {
        homeSubcomponentProvider.destroyHomeSubcomponent()
        super.onCleared()
    }
}