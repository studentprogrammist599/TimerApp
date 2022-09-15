package com.kostry.yourtimer.ui.preset

import com.kostry.yourtimer.di.provider.PresetSubcomponentProvider
import com.kostry.yourtimer.ui.base.BaseViewModel
import javax.inject.Inject

class PresetViewModel @Inject constructor(
    private val provider: PresetSubcomponentProvider,
) : BaseViewModel() {

    private var testIndex = 1

    private val _preset = mutableListOf(TimeCardModel("${(0..999).random()}", (0..99).random(), (0..99).random(), (0..99).random(), (0..99).random()))
    val preset: List<TimeCardModel>
        get() = _preset

    fun addCard() {
        val card = TimeCardModel("${(0..999).random()}", (0..99).random(), (0..99).random(), (0..99).random(), (0..99).random())
        testIndex += 1
        _preset.add(card)
    }

    fun deleteCard(card: TimeCardModel) {
        _preset.remove(card)
    }

    override fun onCleared() {
        super.onCleared()
        provider.destroyPresetSubcomponent()
    }
}