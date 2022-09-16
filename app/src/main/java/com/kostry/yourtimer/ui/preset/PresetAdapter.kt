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
            R.id.item_preset_move_up_button -> {
                actionListener.onMove(card, -1)
            }
            R.id.item_preset_move_down_button -> {
                actionListener.onMove(card, 1)
            }
            else -> {}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PresetViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPresetBinding.inflate(inflater, parent, false)
        binding.itemPresetDeleteButton.setOnClickListener(this)
        binding.itemPresetMoveUpButton.setOnClickListener(this)
        binding.itemPresetMoveDownButton.setOnClickListener(this)
        return PresetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PresetViewHolder, position: Int) {
        val card = cards[position]
        with(holder.binding) {
            itemPresetDeleteButton.tag = card
            itemPresetMoveUpButton.tag = card
            itemPresetMoveDownButton.tag = card
            card.name?.let { itemPresetTextNameEditText.setText(it) }
            card.reps?.let { itemPresetRepsEditText.setText(it.toString()) }
            card.hours?.let { itemPresetHoursEditText.setText(it.toString()) }
            card.minutes?.let { itemPresetMinutesEditText.setText(it.toString()) }
            card.seconds?.let { itemPresetSecondsEditText.setText(it.toString()) }
        }
    }

    override fun getItemCount(): Int = cards.size

    class PresetViewHolder(
        val binding: ItemPresetBinding,
    ) : RecyclerView.ViewHolder(binding.root)
}