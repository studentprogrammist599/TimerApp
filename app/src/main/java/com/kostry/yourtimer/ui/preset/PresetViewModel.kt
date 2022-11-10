package com.kostry.yourtimer.ui.preset

import com.kostry.yourtimer.datasource.DatasourceRepository
import com.kostry.yourtimer.datasource.models.PresetModel
import com.kostry.yourtimer.datasource.models.TimeCardModel
import com.kostry.yourtimer.di.provider.PresetSubcomponentProvider
import com.kostry.yourtimer.ui.base.BaseViewModel
import com.kostry.yourtimer.util.mapTimeToMillis
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class PresetViewModel @Inject constructor(
    private val provider: PresetSubcomponentProvider,
    private val repository: DatasourceRepository
) : BaseViewModel() {

    private var presetId = 0
    private var index = 0
    private var _timeCards= MutableStateFlow(mutableListOf(TimeCardModel(id = index--)))
    var timeCards: StateFlow<List<TimeCardModel>> = _timeCards.asStateFlow()

    fun presetFromArgs(presetModel: PresetModel){
        presetId = presetModel.id
        _timeCards.value = presetModel.timeCards.toMutableList()
    }

    fun addCard() {
        val card = TimeCardModel(id = index--)
        baseViewModelScope.launch {
            val oldList = ArrayList(_timeCards.value)
            oldList.add(card)
            _timeCards.value = oldList
        }
    }

    fun deleteCard(card: TimeCardModel) {
        val indexToDelete = _timeCards.value.indexOfFirst {
            it.id == card.id
        }
        if (indexToDelete != -1) {
            baseViewModelScope.launch {
                val oldList = ArrayList(_timeCards.value)
                oldList.removeAt(indexToDelete)
                _timeCards.value = oldList
            }
        }
    }

    fun moveCard(card: TimeCardModel, moveBy: Int) {
        val oldIndex = _timeCards.value.indexOfFirst {
            it.id == card.id
        }
        if (oldIndex == -1) {
            return
        }
        val newIndex = oldIndex + moveBy
        if (newIndex < 0 || newIndex >= _timeCards.value.size) {
            return
        }
        baseViewModelScope.launch {
            val oldList = ArrayList(_timeCards.value)
            oldList.removeAt(oldIndex)
            oldList.add(oldIndex, card)
            Collections.swap(oldList, oldIndex, newIndex)
            _timeCards.value = oldList
        }
    }

    fun savePreset(name: String): Boolean {
        if (_timeCards.value.isNotEmpty() && name.isNotEmpty() && cardFieldsIsEmpty()) {
            _timeCards.value.forEachIndexed { index, timeCardModel ->
                timeCardModel.enqueue = index
                if (timeCardModel.id <= 0) {
                    timeCardModel.id = 0
                }
            }
            val presetModel = PresetModel(
                id = presetId,
                name = name,
                timeCards = _timeCards.value
            )
            baseViewModelScope.launch {
                repository.savePreset(presetModel)
            }
            return true
        } else {
            return false
        }
    }

    fun cardTextChange(cardModel: TimeCardModel) {
        val indexToChange = _timeCards.value.indexOfFirst {
            it.id == cardModel.id
        }
        if (indexToChange != -1) {
            baseViewModelScope.launch {
                _timeCards.value[indexToChange] = cardModel
            }
        }
    }

    private fun cardFieldsIsEmpty(): Boolean {
        val filtered = _timeCards.value.filterNot { cardModel ->
            cardModel.name.isNullOrEmpty()
        }.filterNot { cardModel ->
            cardModel.reps == null || cardModel.reps == 0
        }.filterNot { cardModel ->
            mapTimeToMillis(
                cardModel.hours ?: 0,
                cardModel.minutes ?: 0,
                cardModel.seconds ?: 0
            ) <= 0L
        }
        return _timeCards.value.size == filtered.size
    }

    override fun onCleared() {
        super.onCleared()
        provider.destroyPresetSubcomponent()
    }
}