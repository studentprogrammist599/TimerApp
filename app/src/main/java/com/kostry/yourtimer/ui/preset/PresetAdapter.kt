package com.kostry.yourtimer.ui.preset

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.kostry.yourtimer.R
import com.kostry.yourtimer.databinding.ItemTimeCardWithButtonsBinding
import com.kostry.yourtimer.datasource.models.TimeCardModel
import com.kostry.yourtimer.util.MinMaxTimeFilter
import com.kostry.yourtimer.util.intSubTimeStringFormat
import com.kostry.yourtimer.util.mapTimeToMillis

interface PresetAdapterListener {
    fun onMove(cardModel: TimeCardModel, moveBy: Int)
    fun onTextChange(cardModel: TimeCardModel)
    fun onDelete(cardModel: TimeCardModel)
}

private const val MOVE_UP = -1
private const val MOVE_DOWN = -1

class PresetAdapter(
    private val listener: PresetAdapterListener,
) : ListAdapter<TimeCardModel, PresetAdapter.PresetViewHolder>(PresetDiffCallback) {

    private val adapterItems = mutableSetOf<ItemTimeCardWithButtonsBinding>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PresetViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTimeCardWithButtonsBinding.inflate(inflater, parent, false)
        return PresetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PresetViewHolder, position: Int) {
        val card = getItem(position)
        adapterItems.add(holder.binding)
        setTextChangeListeners(card.id, holder.binding)
        setButtonListeners(card.id, holder.binding)
        setTextChangedListener(holder.binding)
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

    private fun setButtonListeners(cardId: Int, binding: ItemTimeCardWithButtonsBinding) {
        binding.itemTimeCardWithButtonsMoveUpButton.setOnClickListener {
            listener.onMove(getActualTimeCard(cardId, binding), MOVE_UP)
        }
        binding.itemTimeCardWithButtonsMoveDownButton.setOnClickListener {
            listener.onMove(getActualTimeCard(cardId, binding), MOVE_DOWN)
        }
        binding.itemTimeCardWithButtonsDeleteButton.setOnClickListener {
            listener.onDelete(getActualTimeCard(cardId, binding))
            adapterItems.remove(binding)
        }
    }

    private fun setTextChangeListeners(cardId: Int, binding: ItemTimeCardWithButtonsBinding) {
        binding.itemTimeCardWithButtonsTextNameEditText.addTextChangedListener {
            listener.onTextChange(getActualTimeCard(cardId, binding))
        }
        binding.itemTimeCardWithButtonsRepsEditText.addTextChangedListener {
            listener.onTextChange(getActualTimeCard(cardId, binding))
        }
        binding.itemTimeCardWithButtonsHoursEditText.addTextChangedListener {
            listener.onTextChange(getActualTimeCard(cardId, binding))
        }
        binding.itemTimeCardWithButtonsMinutesEditText.addTextChangedListener {
            listener.onTextChange(getActualTimeCard(cardId, binding))
        }
        binding.itemTimeCardWithButtonsSecondsEditText.addTextChangedListener {
            listener.onTextChange(getActualTimeCard(cardId, binding))
        }
    }

    private fun getActualTimeCard(
        cardId: Int,
        binding: ItemTimeCardWithButtonsBinding
    ): TimeCardModel {
        return TimeCardModel(
            id = cardId,
            name = binding.itemTimeCardWithButtonsTextNameEditText.text.toString(),
            reps = binding.itemTimeCardWithButtonsRepsEditText.text.toString().toIntOrNull(),
            hours = binding.itemTimeCardWithButtonsHoursEditText.text.toString().toIntOrNull(),
            minutes = binding.itemTimeCardWithButtonsMinutesEditText.text.toString()
                .toIntOrNull(),
            seconds = binding.itemTimeCardWithButtonsSecondsEditText.text.toString()
                .toIntOrNull(),
        )
    }

    private fun setTextChangedListener(binding: ItemTimeCardWithButtonsBinding) {
        binding.itemTimeCardWithButtonsTextNameEditText.addTextChangedListener {
            setBoxStrokeColor(
                binding.itemTimeCardWithButtonsTextNameInputLayout,
                color = R.color.text_input_layout_stroke_color
            )
        }
        binding.itemTimeCardWithButtonsRepsEditText.addTextChangedListener {
            setBoxStrokeColor(
                binding.itemTimeCardWithButtonsRepsInputLayout,
                color = R.color.text_input_layout_stroke_color
            )
        }
        binding.itemTimeCardWithButtonsHoursEditText.addTextChangedListener {
            setBoxStrokeColor(
                binding.itemTimeCardWithButtonsHoursTextInputLayout,
                binding.itemTimeCardWithButtonsMinutesTextInputLayout,
                binding.itemTimeCardWithButtonsSecondsTextInputLayout,
                color = R.color.text_input_layout_stroke_color
            )
        }
        binding.itemTimeCardWithButtonsMinutesEditText.addTextChangedListener {
            setBoxStrokeColor(
                binding.itemTimeCardWithButtonsHoursTextInputLayout,
                binding.itemTimeCardWithButtonsMinutesTextInputLayout,
                binding.itemTimeCardWithButtonsSecondsTextInputLayout,
                color = R.color.text_input_layout_stroke_color
            )
        }
        binding.itemTimeCardWithButtonsSecondsEditText.addTextChangedListener {
            setBoxStrokeColor(
                binding.itemTimeCardWithButtonsHoursTextInputLayout,
                binding.itemTimeCardWithButtonsMinutesTextInputLayout,
                binding.itemTimeCardWithButtonsSecondsTextInputLayout,
                color = R.color.text_input_layout_stroke_color
            )
        }
    }

    private fun setBoxStrokeColor(vararg inputLayout: TextInputLayout, color: Int) {
        inputLayout.forEach {
            it.setBoxStrokeColorStateList(
                AppCompatResources.getColorStateList(it.context, color)
            )
        }
    }

    fun checkTimeCardNameIsEmpty(): Boolean {
        var returnedBoolean = true
        adapterItems.forEach { item ->
            if (item.itemTimeCardWithButtonsTextNameEditText.text.toString().isEmpty()) {
                setBoxStrokeColor(
                    item.itemTimeCardWithButtonsTextNameInputLayout,
                    color = R.color.text_input_layout_stroke_error_color
                )
                returnedBoolean = false
            }
        }
        return returnedBoolean
    }

    fun checkTimeCardRepsIsEmpty(): Boolean {
        var returnedBoolean = true
        adapterItems.forEach { item ->
            if (item.itemTimeCardWithButtonsRepsEditText.text.toString().isEmpty()) {
                setBoxStrokeColor(
                    item.itemTimeCardWithButtonsRepsInputLayout,
                    color = R.color.text_input_layout_stroke_error_color
                )
                returnedBoolean = false
            }
        }
        return returnedBoolean
    }

    fun checkTimeCardTimeIsEmpty(): Boolean {
        var returnedBoolean = true
        adapterItems.forEach { item ->
            val hour = item.itemTimeCardWithButtonsHoursEditText.text.toString().toIntOrNull() ?: 0
            val min = item.itemTimeCardWithButtonsMinutesEditText.text.toString().toIntOrNull() ?: 0
            val sec = item.itemTimeCardWithButtonsSecondsEditText.text.toString().toIntOrNull() ?: 0
            if (mapTimeToMillis(hour, min, sec) == 0L) {
                setBoxStrokeColor(
                    item.itemTimeCardWithButtonsHoursTextInputLayout,
                    item.itemTimeCardWithButtonsMinutesTextInputLayout,
                    item.itemTimeCardWithButtonsSecondsTextInputLayout,
                    color = R.color.text_input_layout_stroke_error_color
                )
                returnedBoolean = false
            }
        }
        return returnedBoolean
    }

    inner class PresetViewHolder(
        val binding: ItemTimeCardWithButtonsBinding,
    ) : RecyclerView.ViewHolder(binding.root)

    companion object PresetDiffCallback : DiffUtil.ItemCallback<TimeCardModel>() {
        override fun areItemsTheSame(oldItem: TimeCardModel, newItem: TimeCardModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TimeCardModel, newItem: TimeCardModel): Boolean {
            return oldItem == newItem
        }
    }
}