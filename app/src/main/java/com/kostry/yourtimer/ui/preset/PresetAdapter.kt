package com.kostry.yourtimer.ui.preset

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kostry.yourtimer.R
import com.kostry.yourtimer.databinding.ItemPresetBinding

class PresetAdapter(
    private val listener: TimeCardAdapterListener
) : ListAdapter<TimeCardModel, PresetAdapter.PresetViewHolder>(UsersCallback), View.OnClickListener {

    interface TimeCardAdapterListener{
        fun onDelete(timeCardModel: TimeCardModel)
    }


    override fun onClick(v: View) {
        val card = v.tag as TimeCardModel
        when(v.id){
            R.id.item_preset_delete_button -> listener.onDelete(card)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PresetViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPresetBinding.inflate(inflater, parent, false)
        binding.itemPresetDeleteButton.setOnClickListener(this)
        return PresetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PresetViewHolder, position: Int) {
        val card = getItem(position)
        with(holder.viewBinding){
            itemPresetDeleteButton.tag = card
            itemPresetTextNameEditText.setText(card.name)
            itemPresetRepsEditText.setText(card.reps.toString())
            itemPresetHoursEditText.setText(card.hours.toString())
            itemPresetMinutesEditText.setText(card.minutes.toString())
            itemPresetSecondsEditText.setText(card.seconds.toString())
        }
    }

    class PresetViewHolder(
        val viewBinding: ItemPresetBinding
    ) : RecyclerView.ViewHolder(viewBinding.root)

    companion object UsersCallback : DiffUtil.ItemCallback<TimeCardModel>() {
        override fun areItemsTheSame(
            oldItem: TimeCardModel,
            newItem: TimeCardModel,
        ): Boolean {
            return oldItem.reps == newItem.reps
        }

        override fun areContentsTheSame(
            oldItem: TimeCardModel,
            newItem: TimeCardModel,
        ): Boolean {
            return oldItem == newItem
        }
    }
}