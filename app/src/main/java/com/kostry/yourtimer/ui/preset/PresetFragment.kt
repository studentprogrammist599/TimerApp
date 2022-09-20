package com.kostry.yourtimer.ui.preset

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kostry.yourtimer.databinding.FragmentPresetBinding
import com.kostry.yourtimer.datasource.models.TimeCardModel
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
    private val adapter: TimeCardAdapter by lazy {
        TimeCardAdapter(actionListener = object : TimeCardActionListener {
            override fun onMove(cardModel: TimeCardModel, moveBy: Int) {
                viewModel.moveCard(cardModel, moveBy)
            }

            override fun onTextChange(cardModel: TimeCardModel) {
                viewModel.cardTextChange(cardModel)
            }

            override fun onDelete(cardModel: TimeCardModel) {
                viewModel.deleteCard(cardModel)
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
        binding.presetFragmentRecycler.recycledViewPool.setMaxRecycledViews(0, 0)
        viewModel.addListener(listener)
        binding.presetFragmentAddCardButton.setOnClickListener {
            viewModel.addCard()
            adapter.submitList(viewModel.getPreset())
        }
        binding.presetFragmentSaveButton.setOnClickListener {
            val result = viewModel.savePreset(binding.presetFragmentPresetNameEditText.text.toString())
            if(result){
                Toast.makeText(context, "Preset saved", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }else{
                Toast.makeText(context, "Preset fields is empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.removeListener(listener)
    }

    private val listener: PresetListener = {
        adapter.submitList(it)
    }
}