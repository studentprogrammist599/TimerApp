package com.kostry.yourtimer.ui.preset

import com.kostry.yourtimer.datasource.DatasourceRepository
import com.kostry.yourtimer.datasource.models.PresetModel
import com.kostry.yourtimer.datasource.models.TimeCardModel
import com.kostry.yourtimer.di.provider.PresetSubcomponentProvider
import com.kostry.yourtimer.ui.base.BaseViewModel
import com.kostry.yourtimer.util.mapTimeToMillis
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

typealias PresetListener = (cards: List<TimeCardModel>) -> Unit

class PresetViewModel @Inject constructor(
    private val provider: PresetSubcomponentProvider,
    private val repository: DatasourceRepository
) : BaseViewModel() {

    private var index = 0

    private var timeCards = mutableListOf<TimeCardModel>(
        TimeCardModel(id = index--)
    )
    private val listeners = mutableSetOf<PresetListener>()

    fun addCard() {
        timeCards = ArrayList(timeCards)
        val card = TimeCardModel(id = index--)
        timeCards.add(card)
    }

    fun getPreset(): MutableList<TimeCardModel> {
        return timeCards
    }

    fun deleteCard(card: TimeCardModel) {
        val indexToDelete = timeCards.indexOfFirst {
            it.id == card.id
        }
        if (indexToDelete != -1) {
            timeCards = ArrayList(timeCards)
            timeCards.removeAt(indexToDelete)
            notifyChanges()
        }
    }

    fun moveCard(card: TimeCardModel, moveBy: Int) {
        val oldIndex = timeCards.indexOfFirst {
            it.id == card.id
        }
        if (oldIndex == -1) {
            return
        }
        val newIndex = oldIndex + moveBy
        if (newIndex < 0 || newIndex >= timeCards.size) {
            return
        }
        timeCards = ArrayList(timeCards)
        timeCards.removeAt(oldIndex)
        timeCards.add(oldIndex, card)
        Collections.swap(timeCards, oldIndex, newIndex)
        notifyChanges()
    }

    fun addListener(listener: PresetListener) {
        listeners.add(listener)
        listener.invoke(timeCards)
    }

    fun removeListener(listener: PresetListener) {
        listeners.remove(listener)
    }

    private fun notifyChanges() {
        listeners.forEach { it.invoke(timeCards) }
    }

    override fun onCleared() {
        super.onCleared()
        provider.destroyPresetSubcomponent()
    }

    fun savePreset(name: String): Boolean {
        if (timeCards.isNotEmpty() && name.isNotEmpty() && cardFieldsIsEmpty()) {
            timeCards.forEachIndexed { index, timeCardModel ->
                timeCardModel.enqueue = index
                if (timeCardModel.id <= 0) {
                    timeCardModel.id = 0
                }
            }
            val presetModel = PresetModel(
                name = name,
                timeCards = timeCards
            )
            baseViewModelScope.launch {
                repository.savePreset(presetModel)
            }
            return true
        } else {
            return false
        }
    }

    private fun cardFieldsIsEmpty(): Boolean {
        val filtered = timeCards.filterNot { cardModel ->
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
        return timeCards.size == filtered.size
    }

    fun cardTextChange(cardModel: TimeCardModel) {
        val indexToChange = timeCards.indexOfFirst {
            it.id == cardModel.id
        }
        if (indexToChange != -1) {
            timeCards = ArrayList(timeCards)
            timeCards[indexToChange] = cardModel
        }
    }
}