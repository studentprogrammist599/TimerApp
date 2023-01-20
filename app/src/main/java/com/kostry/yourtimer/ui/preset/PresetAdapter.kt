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

interface PresetAdapterListener {
    fun onMove(cardModel: TimeCardModel, moveBy: Int)
    fun onTextChange(cardModel: TimeCardModel)
    fun onDelete(cardModel: TimeCardModel)
}

interface PresetAdapterBindingsCatcher {
    fun catchBinding(binding: ItemTimeCardWithButtonsBinding)
    fun removeBinding(binding: ItemTimeCardWithButtonsBinding)
}

class PresetAdapter(
    private val listener: PresetAdapterListener,
    private val bindingsCatcher: PresetAdapterBindingsCatcher,
) : ListAdapter<TimeCardModel, PresetAdapter.PresetViewHolder>(PresetDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PresetViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTimeCardWithButtonsBinding.inflate(inflater, parent, false)
        return PresetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PresetViewHolder, position: Int) {
        val card = getItem(position)
        setTextChangeListeners(card.id, holder.binding)
        setButtonListeners(card.id, holder.binding)
        setTextChangedListener(holder.binding)
        bindingsCatcher.catchBinding(holder.binding)
        with(holder.binding) {
            itemTimeCardWithButtonsDeleteButton.tag = card
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
            listener.onMove(getActualTimeCard(cardId, binding), -1)
        }
        binding.itemTimeCardWithButtonsMoveDownButton.setOnClickListener {
            listener.onMove(getActualTimeCard(cardId, binding), 1)
        }
        binding.itemTimeCardWithButtonsDeleteButton.setOnClickListener {
            listener.onDelete(getActualTimeCard(cardId, binding))
            bindingsCatcher.removeBinding(binding)
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
        bindingsCatcher.catchBinding(binding)
        binding.itemTimeCardWithButtonsTextNameEditText.addTextChangedListener {
            setDefaultStrokeColor(binding.itemTimeCardWithButtonsTextNameInputLayout)
        }
        binding.itemTimeCardWithButtonsRepsEditText.addTextChangedListener {
            setDefaultStrokeColor(binding.itemTimeCardWithButtonsRepsInputLayout)
        }
        binding.itemTimeCardWithButtonsHoursEditText.addTextChangedListener {
            setDefaultStrokeColor(
                binding.itemTimeCardWithButtonsHoursTextInputLayout,
                binding.itemTimeCardWithButtonsMinutesTextInputLayout,
                binding.itemTimeCardWithButtonsSecondsTextInputLayout
            )
        }
        binding.itemTimeCardWithButtonsMinutesEditText.addTextChangedListener {
            setDefaultStrokeColor(
                binding.itemTimeCardWithButtonsHoursTextInputLayout,
                binding.itemTimeCardWithButtonsMinutesTextInputLayout,
                binding.itemTimeCardWithButtonsSecondsTextInputLayout
            )
        }
        binding.itemTimeCardWithButtonsSecondsEditText.addTextChangedListener {
            setDefaultStrokeColor(
                binding.itemTimeCardWithButtonsHoursTextInputLayout,
                binding.itemTimeCardWithButtonsMinutesTextInputLayout,
                binding.itemTimeCardWithButtonsSecondsTextInputLayout
            )
        }
    }

    private fun setDefaultStrokeColor(vararg inputLayout: TextInputLayout) {
        inputLayout.forEach {
            it.setBoxStrokeColorStateList(
                AppCompatResources.getColorStateList(
                    it.context,
                    R.color.text_input_layout_stroke_color
                )
            )
        }
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