package com.kostry.yourtimer.ui.preset

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.kostry.yourtimer.databinding.FragmentPresetBinding
import com.kostry.yourtimer.ui.base.BaseFragment

class PresetFragment : BaseFragment<FragmentPresetBinding>() {

    private val adapter: PresetAdapter by lazy {
        PresetAdapter(object : PresetAdapter.TimeCardAdapterListener{
            override fun onDelete(timeCardModel: TimeCardModel) {
                Toast.makeText(context, "${timeCardModel.reps}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private var testCounter = 0

    private val testList = mutableListOf<TimeCardModel>()

    override fun getViewBinding(container: ViewGroup?) =
        FragmentPresetBinding.inflate(layoutInflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.presetFragmentRecycler.adapter = adapter
        binding.presetFragmentAddCardButton.setOnClickListener {
            testList.add(TimeCardModel("", testCounter, 0, 0, 0))
            testCounter += 1
            adapter.submitList(testList)
        }
    }
}