package com.kostry.yourtimer.ui.preset

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kostry.yourtimer.R
import com.kostry.yourtimer.databinding.ItemPresetBinding

interface TimeCardActionListener {
    fun onMove(cardModel: TimeCardModel, moveBy: Int)
    fun onDelete(cardModel: TimeCardModel)
}

class PresetAdapter(
    private val actionListener: TimeCardActionListener,
) : RecyclerView.Adapter<PresetAdapter.PresetViewHolder>(), View.OnClickListener {

    var cards: List<TimeCardModel> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onClick(v: View) {
        val card = v.tag as TimeCardModel
        when (v.id) {
            R.id.item_preset_delete_button -> {
                actionListener.onDelete(card)
            }
            else -> {}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PresetViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPresetBinding.inflate(inflater, parent, false)
        binding.itemPresetDeleteButton.setOnClickListener(this)
        return PresetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PresetViewHolder, position: Int) {
        val card = cards[position]
        holder.move(card.id)
        with(holder.binding) {
            itemPresetDeleteButton.tag = card
            card.name?.let { itemPresetTextNameEditText.setText(it) }
            card.reps?.let { itemPresetRepsEditText.setText(it.toString()) }
            card.hours?.let { itemPresetHoursEditText.setText(it.toString()) }
            card.minutes?.let { itemPresetMinutesEditText.setText(it.toString()) }
            card.seconds?.let { itemPresetSecondsEditText.setText(it.toString()) }
        }
    }

    override fun getItemCount(): Int = cards.size

    inner class PresetViewHolder(
        val binding: ItemPresetBinding,
    ) : RecyclerView.ViewHolder(binding.root){

        fun move(cardId: Int){
            binding.itemPresetMoveUpButton.setOnClickListener {
                actionListener.onMove(
                    //создавать TimeCardModel и считывать значения полей только при клике, иначе баг
                    TimeCardModel(
                        id = cardId,
                        name = binding.itemPresetTextNameEditText.text.toString(),
                        reps = binding.itemPresetRepsEditText.text.toString().toIntOrNull(),
                        hours = binding.itemPresetHoursEditText.text.toString().toIntOrNull(),
                        minutes = binding.itemPresetMinutesEditText.text.toString().toIntOrNull(),
                        seconds = binding.itemPresetSecondsEditText.text.toString().toIntOrNull(),
                ), -1)
            }
            binding.itemPresetMoveDownButton.setOnClickListener {
                actionListener.onMove(
                    TimeCardModel(
                        id = cardId,
                        name = binding.itemPresetTextNameEditText.text.toString(),
                        reps = binding.itemPresetRepsEditText.text.toString().toIntOrNull(),
                        hours = binding.itemPresetHoursEditText.text.toString().toIntOrNull(),
                        minutes = binding.itemPresetMinutesEditText.text.toString().toIntOrNull(),
                        seconds = binding.itemPresetSecondsEditText.text.toString().toIntOrNull(),
                    ), 1)
            }
        }
    }
}