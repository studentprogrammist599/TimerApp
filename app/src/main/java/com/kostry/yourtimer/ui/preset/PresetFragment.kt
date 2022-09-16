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
import com.kostry.yourtimer.util.mapTimeToMillis
import javax.inject.Inject

class PresetFragment : BaseFragment<FragmentPresetBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: PresetViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PresetViewModel::class.java]
    }
    private val adapter: PresetAdapter by lazy {
        PresetAdapter(actionListener = object : TimeCardActionListener {
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
        binding.presetFragmentRecycler.recycledViewPool.setMaxRecycledViews(0,0)
        viewModel.addListener(listener)
        binding.presetFragmentAddCardButton.setOnClickListener {
            viewModel.addCard()
            adapter.cards = viewModel.getPreset()
        }
        binding.presetFragmentSaveButton.setOnClickListener {
            viewModel.getPreset().forEach { card ->
                if (!fieldsIsEmpty(card)){
                    Toast.makeText(context, "EMPTY", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context, "SAVE", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun fieldsIsEmpty(cardModel: TimeCardModel): Boolean {
        when {
            cardModel.name.isNullOrEmpty() -> {
                return false
            }
            cardModel.reps == null || cardModel.reps == 0 -> {
                return false
            }
            mapTimeToMillis(cardModel.hours ?: 0, cardModel.minutes ?: 0, cardModel.seconds ?: 0) == 0L -> {
                return false
            }
            binding.presetFragmentPresetNameEditText.text.toString().isEmpty() ->{
                return false
            }
            else -> {
                return true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.removeListener(listener)
    }

    private val listener: PresetListener = {
        adapter.cards = it
    }
}