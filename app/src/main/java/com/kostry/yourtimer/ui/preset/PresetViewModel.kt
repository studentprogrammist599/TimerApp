package com.kostry.yourtimer.ui.preset

import com.kostry.yourtimer.di.provider.PresetSubcomponentProvider
import com.kostry.yourtimer.ui.base.BaseViewModel
import java.util.*
import javax.inject.Inject

typealias PresetListener = (cards: List<TimeCardModel>) -> Unit

class PresetViewModel @Inject constructor(
    private val provider: PresetSubcomponentProvider,
) : BaseViewModel() {

    private var cardId = 0

    private var preset = mutableListOf<TimeCardModel>(
        TimeCardModel(101,"AAAA", 11, 11, 11, 11),
        TimeCardModel(102,"BBBB", 22, 22, 22, 22),
        TimeCardModel(103,"CCCC", 33, 33, 33, 33),
    )
    private val listeners = mutableSetOf<PresetListener>()

    fun addCard() {
        val card = TimeCardModel(cardId,"${(0..999).random()}", (0..99).random(), (0..99).random(), (0..99).random(), (0..99).random())
        cardId += 1
        preset.add(card)
    }

    fun getPreset(): MutableList<TimeCardModel> {
        return preset
    }

    fun deleteCard(card: TimeCardModel) {
        val indexToDelete = preset.indexOfFirst {
            it.id == card.id
        }
        if (indexToDelete != -1) {
            preset.removeAt(indexToDelete)
            notifyChanges()
        }
    }

    fun moveCard(card: TimeCardModel, moveBy: Int){
        val oldIndex = preset.indexOfFirst {
            it.id == card.id
        }
        if (oldIndex == -1){
            return
        }
        val newIndex = oldIndex + moveBy
        if (newIndex < 0 || newIndex >= preset.size){
            return
        }
        Collections.swap(preset, oldIndex, newIndex)
        notifyChanges()
    }

    fun addListener(listener: PresetListener){
        listeners.add(listener)
        listener.invoke(preset)
    }

    fun removeListener(listener: PresetListener){
        listeners.remove(listener)
    }

    private fun notifyChanges(){
        listeners.forEach { it.invoke(preset) }
    }

    override fun onCleared() {
        super.onCleared()
        provider.destroyPresetSubcomponent()
    }
}