package com.kostry.yourtimer.ui.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kostry.yourtimer.R
import com.kostry.yourtimer.databinding.ItemTimeCardBinding

class TimeCardAdapter(
    private val listener: TimeCardAdapterListener
) : ListAdapter<TimeCardModel, TimeCardAdapter.TimeCardViewHolder>(UsersCallback), View.OnClickListener {

    interface TimeCardAdapterListener{
        fun onDelete(timeCardModel: TimeCardModel)
    }


    override fun onClick(v: View) {
        val card = v.tag as TimeCardModel
        when(v.id){
            R.id.item_time_card_delete_button -> listener.onDelete(card)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeCardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTimeCardBinding.inflate(inflater, parent, false)
        binding.itemTimeCardDeleteButton.setOnClickListener(this)
        return TimeCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TimeCardViewHolder, position: Int) {
        val card = getItem(position)
        with(holder.viewBinding){
            itemTimeCardDeleteButton.tag = card
            itemTimeCardTextNameEditText.setText(card.name)
        }
    }

    class TimeCardViewHolder(
        val viewBinding: ItemTimeCardBinding
    ) : RecyclerView.ViewHolder(viewBinding.root)

    companion object UsersCallback : DiffUtil.ItemCallback<TimeCardModel>() {
        override fun areItemsTheSame(
            oldItem: TimeCardModel,
            newItem: TimeCardModel,
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: TimeCardModel,
            newItem: TimeCardModel,
        ): Boolean {
            return oldItem == newItem
        }
    }
}