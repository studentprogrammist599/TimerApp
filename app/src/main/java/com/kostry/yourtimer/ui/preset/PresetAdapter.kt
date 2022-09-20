package com.kostry.yourtimer.ui.preset

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kostry.yourtimer.R
import com.kostry.yourtimer.databinding.ItemTimeCardWithButtonsBinding
import com.kostry.yourtimer.datasource.models.TimeCardModel
import com.kostry.yourtimer.util.MinMaxTimeFilter
import com.kostry.yourtimer.util.intSubTimeStringFormat

interface TimeCardActionListener {
    fun onMove(cardModel: TimeCardModel, moveBy: Int)
    fun onTextChange(cardModel: TimeCardModel)
    fun onDelete(cardModel: TimeCardModel)
}

class TimeCardAdapter(
    private val actionListener: TimeCardActionListener,
) : ListAdapter<TimeCardModel, TimeCardAdapter.PresetViewHolder>(PresetDiffCallback),
    View.OnClickListener {

    override fun onClick(v: View) {
        val card = v.tag as TimeCardModel
        when (v.id) {
            R.id.item_time_card_with_buttons_delete_button -> {
                actionListener.onDelete(card)
            }
            else -> {}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PresetViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTimeCardWithButtonsBinding.inflate(inflater, parent, false)
        binding.itemTimeCardWithButtonsDeleteButton.setOnClickListener(this)
        return PresetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PresetViewHolder, position: Int) {
        val card = getItem(position)
        holder.move(card.id)
        holder.textChangeListeners(card.id)
        holder.binding.itemTimeCardWithButtonsDeleteButton.tag = card
        with(holder.binding) {
            itemTimeCardWithButtonsMinutesEditText.filters = arrayOf(MinMaxTimeFilter())
            itemTimeCardWithButtonsSecondsEditText.filters = arrayOf(MinMaxTimeFilter())
            card.name?.let { itemTimeCardWithButtonsTextNameEditText.setText(it) }
            card.reps?.let { itemTimeCardWithButtonsRepsEditText.setText(it.intSubTimeStringFormat()) }
            card.hours?.let { itemTimeCardWithButtonsHoursEditText.setText(it.intSubTimeStringFormat()) }
            card.minutes?.let { itemTimeCardWithButtonsMinutesEditText.setText(it.intSubTimeStringFormat()) }
            card.seconds?.let { itemTimeCardWithButtonsSecondsEditText.setText(it.intSubTimeStringFormat()) }
        }
    }

    inner class PresetViewHolder(
        val binding: ItemTimeCardWithButtonsBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        private fun getNewTimeCard(cardId: Int){}

        fun move(cardId: Int) {
            binding.itemTimeCardWithButtonsMoveUpButton.setOnClickListener {
                actionListener.onMove(
                    //создавать TimeCardModel и считывать значения полей только при клике, иначе баг
                    TimeCardModel(
                        id = cardId,
                        name = binding.itemTimeCardWithButtonsTextNameEditText.text.toString(),
                        reps = binding.itemTimeCardWithButtonsRepsEditText.text.toString()
                            .toIntOrNull(),
                        hours = binding.itemTimeCardWithButtonsHoursEditText.text.toString()
                            .toIntOrNull(),
                        minutes = binding.itemTimeCardWithButtonsMinutesEditText.text.toString()
                            .toIntOrNull(),
                        seconds = binding.itemTimeCardWithButtonsSecondsEditText.text.toString()
                            .toIntOrNull(),
                    ), -1
                )
            }
            binding.itemTimeCardWithButtonsMoveDownButton.setOnClickListener {
                actionListener.onMove(
                    TimeCardModel(
                        id = cardId,
                        name = binding.itemTimeCardWithButtonsTextNameEditText.text.toString(),
                        reps = binding.itemTimeCardWithButtonsRepsEditText.text.toString()
                            .toIntOrNull(),
                        hours = binding.itemTimeCardWithButtonsHoursEditText.text.toString()
                            .toIntOrNull(),
                        minutes = binding.itemTimeCardWithButtonsMinutesEditText.text.toString()
                            .toIntOrNull(),
                        seconds = binding.itemTimeCardWithButtonsSecondsEditText.text.toString()
                            .toIntOrNull(),
                    ), 1
                )
            }
        }

        fun textChangeListeners(cardId: Int) {
            binding.itemTimeCardWithButtonsTextNameEditText.addTextChangedListener {
                val changedCard = TimeCardModel(
                    id = cardId,
                    name = binding.itemTimeCardWithButtonsTextNameEditText.text.toString(),
                    reps = binding.itemTimeCardWithButtonsRepsEditText.text.toString()
                        .toIntOrNull(),
                    hours = binding.itemTimeCardWithButtonsHoursEditText.text.toString()
                        .toIntOrNull(),
                    minutes = binding.itemTimeCardWithButtonsMinutesEditText.text.toString()
                        .toIntOrNull(),
                    seconds = binding.itemTimeCardWithButtonsSecondsEditText.text.toString()
                        .toIntOrNull(),
                )
                actionListener.onTextChange(changedCard)
            }
            binding.itemTimeCardWithButtonsRepsEditText.addTextChangedListener {
                val changedCard = TimeCardModel(
                    id = cardId,
                    name = binding.itemTimeCardWithButtonsTextNameEditText.text.toString(),
                    reps = binding.itemTimeCardWithButtonsRepsEditText.text.toString()
                        .toIntOrNull(),
                    hours = binding.itemTimeCardWithButtonsHoursEditText.text.toString()
                        .toIntOrNull(),
                    minutes = binding.itemTimeCardWithButtonsMinutesEditText.text.toString()
                        .toIntOrNull(),
                    seconds = binding.itemTimeCardWithButtonsSecondsEditText.text.toString()
                        .toIntOrNull(),
                )
                actionListener.onTextChange(changedCard)
            }
            binding.itemTimeCardWithButtonsHoursEditText.addTextChangedListener {
                val changedCard = TimeCardModel(
                    id = cardId,
                    name = binding.itemTimeCardWithButtonsTextNameEditText.text.toString(),
                    reps = binding.itemTimeCardWithButtonsRepsEditText.text.toString()
                        .toIntOrNull(),
                    hours = binding.itemTimeCardWithButtonsHoursEditText.text.toString()
                        .toIntOrNull(),
                    minutes = binding.itemTimeCardWithButtonsMinutesEditText.text.toString()
                        .toIntOrNull(),
                    seconds = binding.itemTimeCardWithButtonsSecondsEditText.text.toString()
                        .toIntOrNull(),
                )
                actionListener.onTextChange(changedCard)
            }
            binding.itemTimeCardWithButtonsMinutesEditText.addTextChangedListener {
                val changedCard = TimeCardModel(
                    id = cardId,
                    name = binding.itemTimeCardWithButtonsTextNameEditText.text.toString(),
                    reps = binding.itemTimeCardWithButtonsRepsEditText.text.toString()
                        .toIntOrNull(),
                    hours = binding.itemTimeCardWithButtonsHoursEditText.text.toString()
                        .toIntOrNull(),
                    minutes = binding.itemTimeCardWithButtonsMinutesEditText.text.toString()
                        .toIntOrNull(),
                    seconds = binding.itemTimeCardWithButtonsSecondsEditText.text.toString()
                        .toIntOrNull(),
                )
                actionListener.onTextChange(changedCard)
            }
            binding.itemTimeCardWithButtonsSecondsEditText.addTextChangedListener {
                val changedCard = TimeCardModel(
                    id = cardId,
                    name = binding.itemTimeCardWithButtonsTextNameEditText.text.toString(),
                    reps = binding.itemTimeCardWithButtonsRepsEditText.text.toString()
                        .toIntOrNull(),
                    hours = binding.itemTimeCardWithButtonsHoursEditText.text.toString()
                        .toIntOrNull(),
                    minutes = binding.itemTimeCardWithButtonsMinutesEditText.text.toString()
                        .toIntOrNull(),
                    seconds = binding.itemTimeCardWithButtonsSecondsEditText.text.toString()
                        .toIntOrNull(),
                )
                actionListener.onTextChange(changedCard)
            }
        }
    }

    companion object PresetDiffCallback : DiffUtil.ItemCallback<TimeCardModel>() {
        override fun areItemsTheSame(oldItem: TimeCardModel, newItem: TimeCardModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TimeCardModel, newItem: TimeCardModel): Boolean {
            return oldItem == newItem
        }
    }
}