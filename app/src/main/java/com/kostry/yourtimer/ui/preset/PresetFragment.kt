package com.kostry.yourtimer.ui.preset

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.kostry.yourtimer.databinding.FragmentPresetBinding
import com.kostry.yourtimer.di.provider.PresetSubcomponentProvider
import com.kostry.yourtimer.ui.base.BaseFragment
import com.kostry.yourtimer.util.ViewModelFactory
import javax.inject.Inject

class PresetFragment : BaseFragment<FragmentPresetBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: PresetViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PresetViewModel::class.java]
    }

    private val adapter: PresetAdapter by lazy {
        PresetAdapter(object : PresetAdapter.TimeCardAdapterListener {
            override fun onDelete(timeCardModel: TimeCardModel) {
                viewModel.deleteCard(timeCardModel)
                adapter.notifyDataSetChanged()
            }
        })
    }

    override fun onAttach(context: Context) {
        (requireActivity().application as PresetSubcomponentProvider)
            .iniPresetSubcomponent()
            .inject(this)
        super.onAttach(context)
    }

    override fun getViewBinding(container: ViewGroup?) =
        FragmentPresetBinding.inflate(layoutInflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.presetFragmentRecycler.adapter = adapter
        adapter.submitList(viewModel.preset)
        binding.presetFragmentAddCardButton.setOnClickListener {
            viewModel.addCard()
            adapter.submitList(viewModel.preset)
            adapter.notifyDataSetChanged()
            Toast.makeText(context, viewModel.preset.size.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}