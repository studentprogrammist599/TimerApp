package com.kostry.yourtimer.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kostry.yourtimer.databinding.ItemTimePresetBinding
import com.kostry.yourtimer.datasource.models.PresetModel

class HomeParentAdapter :
    ListAdapter<PresetModel, HomeParentAdapter.ParentViewHolder>(ParentDiffCallback) {

    inner class ParentViewHolder(
        val binding: ItemTimePresetBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTimePresetBinding.inflate(inflater, parent, false)
        return ParentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        val preset = getItem(position)
        val childAdapter = HomeChildAdapter()
        with(holder.binding) {
            itemTimePresetName.text = preset.name
            itemTimePresetRecycler.adapter = childAdapter
            childAdapter.submitList(preset.timeCards)
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