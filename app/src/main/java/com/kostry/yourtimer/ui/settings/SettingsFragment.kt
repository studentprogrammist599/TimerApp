package com.kostry.yourtimer.ui.settings

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.kostry.yourtimer.databinding.FragmentSettingsBinding
import com.kostry.yourtimer.ui.base.BaseFragment

class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    private val adapter: TimeCardAdapter by lazy {
        TimeCardAdapter(object : TimeCardAdapter.TimeCardAdapterListener{
            override fun onDelete(timeCardModel: TimeCardModel) {
                Toast.makeText(context, "${timeCardModel.reps}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private var testCounter = 0

    private val testList = mutableListOf<TimeCardModel>()

    override fun getViewBinding(container: ViewGroup?) =
        FragmentSettingsBinding.inflate(layoutInflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.settingFragmentRecycler.adapter = adapter
        binding.settingFragmentAddCardButton.setOnClickListener {
            testList.add(TimeCardModel("", testCounter, 0, 0, 0))
            testCounter += 1
            adapter.submitList(testList)
        }
    }
}