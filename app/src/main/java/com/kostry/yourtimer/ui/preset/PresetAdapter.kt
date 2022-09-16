package com.kostry.yourtimer.ui.preset

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kostry.yourtimer.R
import com.kostry.yourtimer.databinding.ItemTimeCardBinding
import com.kostry.yourtimer.util.intSubTimeStringFormat

interface TimeCardActionListener {
    fun onMove(cardModel: TimeCardModel, moveBy: Int)
    fun onDelete(cardModel: TimeCardModel)
}

class PresetAdapter(
    private val actionListener: TimeCardActionListener,
) : ListAdapter<TimeCardModel, PresetAdapter.TimeCardViewHolder>(TimeCardDiffCallback), View.OnClickListener {


    override fun onClick(v: View) {
        val card = v.tag as TimeCardModel
        when (v.id) {
            R.id.item_time_card_delete_button -> {
                actionListener.onDelete(card)
            }
            else -> {}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeCardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTimeCardBinding.inflate(inflater, parent, false)
        binding.itemTimeCardDeleteButton.setOnClickListener(this)
        return TimeCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TimeCardViewHolder, position: Int) {
        val card: TimeCardModel = currentList[position]
        holder.move(card.id)
        with(holder.binding) {
            itemTimeCardDeleteButton.tag = card
            card.name?.let { itemTimeCardTextNameEditText.setText(it) }
            card.reps?.let { itemTimeCardRepsEditText.setText(it.intSubTimeStringFormat()) }
            card.hours?.let { itemTimeCardHoursEditText.setText(it.intSubTimeStringFormat()) }
            card.minutes?.let { itemTimeCardMinutesEditText.setText(it.intSubTimeStringFormat()) }
            card.seconds?.let { itemTimeCardSecondsEditText.setText(it.intSubTimeStringFormat()) }
        }
    }

    inner class TimeCardViewHolder(
        val binding: ItemTimeCardBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun move(cardId: Int) {
            binding.itemTimeCardMoveUpButton.setOnClickListener {
                actionListener.onMove(
                    //создавать TimeCardModel и считывать значения полей только при клике, иначе баг
                    TimeCardModel(
                        id = cardId,
                        name = binding.itemTimeCardTextNameEditText.text.toString(),
                        reps = binding.itemTimeCardRepsEditText.text.toString().toIntOrNull(),
                        hours = binding.itemTimeCardHoursEditText.text.toString().toIntOrNull(),
                        minutes = binding.itemTimeCardMinutesEditText.text.toString().toIntOrNull(),
                        seconds = binding.itemTimeCardSecondsEditText.text.toString().toIntOrNull(),
                    ), -1)
            }
            binding.itemTimeCardMoveDownButton.setOnClickListener {
                actionListener.onMove(
                    TimeCardModel(
                        id = cardId,
                        name = binding.itemTimeCardTextNameEditText.text.toString(),
                        reps = binding.itemTimeCardRepsEditText.text.toString().toIntOrNull(),
                        hours = binding.itemTimeCardHoursEditText.text.toString().toIntOrNull(),
                        minutes = binding.itemTimeCardMinutesEditText.text.toString().toIntOrNull(),
                        seconds = binding.itemTimeCardSecondsEditText.text.toString().toIntOrNull(),
                    ), 1)
            }
        }
    }

    companion object TimeCardDiffCallback: DiffUtil.ItemCallback<TimeCardModel>() {
        override fun areItemsTheSame(oldItem: TimeCardModel, newItem: TimeCardModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TimeCardModel, newItem: TimeCardModel): Boolean {
            return oldItem == newItem
        }
    }
}