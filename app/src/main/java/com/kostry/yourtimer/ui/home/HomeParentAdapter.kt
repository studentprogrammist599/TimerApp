package com.kostry.yourtimer.ui.home

import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kostry.yourtimer.R
import com.kostry.yourtimer.databinding.ItemTimePresetBinding
import com.kostry.yourtimer.datasource.models.PresetModel

interface HomeParentAdapterListener {
    fun onDelete(presetModel: PresetModel)
    fun onEdit(presetModel: PresetModel)
}

private const val ID_ON_DELETE = 1
private const val ID_ON_EDIT = 2

class HomeParentAdapter(
    private val listener: HomeParentAdapterListener
) : ListAdapter<PresetModel, HomeParentAdapter.ParentViewHolder>(ParentDiffCallback), View.OnClickListener {

    class ParentViewHolder(
        val binding: ItemTimePresetBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onClick(v: View) {
        when(v.id) {
            R.id.item_time_preset_options_button -> showPopupMenu(v)
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
            itemTimePresetOptionsButton.tag = presetModel

            itemTimePresetName.text = presetModel.name
            itemTimePresetRecycler.adapter = childAdapter
            childAdapter.submitList(presetModel.timeCards)
        }
    }

    private fun showPopupMenu(view: View){
        val presetModel = view.tag as PresetModel
        val popupMenu = PopupMenu(view.context, view)

        popupMenu.menu.add(0, ID_ON_DELETE, Menu.NONE, view.context.getString(R.string.delete))
        popupMenu.menu.add(0, ID_ON_EDIT, Menu.NONE, view.context.getString(R.string.edit))

        popupMenu.setOnMenuItemClickListener {
            when(it.itemId){
                ID_ON_DELETE -> {
                    listener.onDelete(presetModel)
                }
                ID_ON_EDIT -> {
                    listener.onEdit(presetModel)
                }
            }
            return@setOnMenuItemClickListener true
        }

        popupMenu.show()
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