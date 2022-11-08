package com.kostry.yourtimer.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kostry.yourtimer.databinding.ItemTimeCardNoButtonsBinding
import com.kostry.yourtimer.datasource.models.TimeCardModel
import com.kostry.yourtimer.util.intSubTimeStringFormat

class HomeChildAdapter :
    ListAdapter<TimeCardModel, HomeChildAdapter.ChildViewHolder>(ChildDiffCallback) {

    class ChildViewHolder(
        val binding: ItemTimeCardNoButtonsBinding,
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTimeCardNoButtonsBinding.inflate(inflater, parent, false)
        return ChildViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        val card = getItem(position)
        with(holder.binding) {
            itemTimeCardTextName.text = card.name
            itemTimeCardRepsCountTextView.text = card.reps?.intSubTimeStringFormat()
            itemTimeCardHoursCountTextView.text = card.hours?.intSubTimeStringFormat()
            itemTimeCardMinutesCountTextView.text = card.minutes?.intSubTimeStringFormat()
            itemTimeCardSecondsCountTextView.text = card.seconds?.intSubTimeStringFormat()
        }
    }

    companion object ChildDiffCallback : DiffUtil.ItemCallback<TimeCardModel>() {
        override fun areItemsTheSame(oldItem: TimeCardModel, newItem: TimeCardModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TimeCardModel, newItem: TimeCardModel): Boolean {
            return oldItem == newItem
        }
    }
}