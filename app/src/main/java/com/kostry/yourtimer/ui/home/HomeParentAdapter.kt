package com.kostry.yourtimer.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kostry.yourtimer.R
import com.kostry.yourtimer.databinding.ItemTimePresetBinding
import com.kostry.yourtimer.datasource.models.PresetModel

class HomeParentAdapter(
    private val listener: HomeParentAdapterListener
) : ListAdapter<PresetModel, HomeParentAdapter.ParentViewHolder>(ParentDiffCallback), View.OnClickListener {

    interface HomeParentAdapterListener {
        fun onDelete(presetModel: PresetModel)
    }

    class ParentViewHolder(
        val binding: ItemTimePresetBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onClick(v: View) {
        val presetModel = v.tag as PresetModel
        when(v.id) {
            R.id.item_time_preset_options_button -> listener.onDelete(presetModel)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTimePresetBinding.inflate(inflater, parent, false)

        binding.itemTimePresetOptionsButton.setOnClickListener(this)

        return ParentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        val presetModel = getItem(position)
        val childAdapter = HomeChildAdapter()
        with(holder.binding) {
            root.tag = presetModel
            itemTimePresetOptionsButton.tag = presetModel

            itemTimePresetName.text = presetModel.name
            itemTimePresetRecycler.adapter = childAdapter
            childAdapter.submitList(presetModel.timeCards)
        }
    }

    companion object ParentDiffCallback : DiffUtil.ItemCallback<PresetModel>() {
        override fun areItemsTheSame(oldItem: PresetModel, newItem: PresetModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PresetModel, newItem: PresetModel): Boolean {
            return oldItem == newItem
        }
    }
}