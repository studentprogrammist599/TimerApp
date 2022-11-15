package com.kostry.yourtimer.ui.preset

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputLayout
import com.kostry.yourtimer.R
import com.kostry.yourtimer.databinding.FragmentPresetBinding
import com.kostry.yourtimer.databinding.ItemTimeCardWithButtonsBinding
import com.kostry.yourtimer.datasource.models.TimeCardModel
import com.kostry.yourtimer.di.provider.PresetSubcomponentProvider
import com.kostry.yourtimer.ui.base.BaseFragment
import com.kostry.yourtimer.util.ViewModelFactory
import com.kostry.yourtimer.util.mapTimeToMillis
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class PresetFragment : BaseFragment<FragmentPresetBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: PresetViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PresetViewModel::class.java]
    }

    private val args by navArgs<PresetFragmentArgs>()
    private val adapterItems = mutableListOf<ItemTimeCardWithButtonsBinding>()

    private val adapter: PresetAdapter by lazy {
        PresetAdapter(
            listener = object : PresetAdapterListener {
                override fun onMove(cardModel: TimeCardModel, moveBy: Int) {
                    viewModel.moveCard(cardModel, moveBy)
                }

                override fun onTextChange(cardModel: TimeCardModel) {
                    viewModel.cardTextChange(cardModel)
                }

                override fun onDelete(cardModel: TimeCardModel) {
                    viewModel.deleteCard(cardModel)
                }
            },
            bindingsCatcher = object : PresetAdapterBindingsCatcher {
                override fun catchBinding(binding: ItemTimeCardWithButtonsBinding) {
                    adapterItems.add(binding)
                }
            }
        )
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
        if (args.preset != null) {
            viewModel.presetFromArgs(args.preset!!)
            binding.presetFragmentPresetNameEditText.setText(args.preset!!.name)
        }
        initAdapter()
        initClickListeners()
        initObserver()
        initTextFieldsListeners()
    }

    private fun initObserver() {
        lifecycleScope.launchWhenCreated {
            viewModel.timeCards.collectLatest {
                adapter.submitList(it)
            }
        }
    }

    private fun initClickListeners() {
        binding.presetFragmentAddCardButton.setOnClickListener {
            viewModel.addCard()
        }
        binding.presetFragmentSaveButton.setOnClickListener {
            val presetNameIsEmpty = checkPresetNameIsEmpty()
            val cardsNamesIsEmpty = checkTimeCardNameIsEmpty()
            val cardsTimesIsEmpty = checkTimeCardTimeIsEmpty()
            val cardsRepsIsEmpty = checkTimeCardRepsIsEmpty()
            if (presetNameIsEmpty && cardsNamesIsEmpty && cardsTimesIsEmpty && cardsRepsIsEmpty) {
                Toast.makeText(context, "Preset saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Preset fields is empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initTextFieldsListeners() {
        binding.presetFragmentPresetNameEditText.addTextChangedListener {
            setBoxStrokeColor(
                binding.presetFragmentTextInputLayout,
                color = R.color.text_input_layout_stroke_color
            )
        }
    }

    private fun checkPresetNameIsEmpty(): Boolean {
        if (binding.presetFragmentPresetNameEditText.text.toString().isEmpty()) {
            setBoxStrokeColor(
                binding.presetFragmentTextInputLayout,
                color = R.color.text_input_layout_stroke_error_color
            )
            return false
        }
        return true
    }

    private fun checkTimeCardNameIsEmpty(): Boolean {
        var returnedBoolean = true
        adapterItems.forEach { item ->
            if (item.itemTimeCardWithButtonsTextNameEditText.text.toString().isEmpty()) {
                setBoxStrokeColor(
                    item.itemTimeCardWithButtonsTextNameInputLayout,
                    color = R.color.text_input_layout_stroke_error_color
                )
                returnedBoolean = false
            }
        }
        return returnedBoolean
    }

    private fun checkTimeCardRepsIsEmpty(): Boolean {
        var returnedBoolean = true
        adapterItems.forEach { item ->
            if (item.itemTimeCardWithButtonsRepsEditText.text.toString().isEmpty()) {
                setBoxStrokeColor(
                    item.itemTimeCardWithButtonsRepsInputLayout,
                    color = R.color.text_input_layout_stroke_error_color
                )
                returnedBoolean = false
            }
        }
        return returnedBoolean
    }

    private fun checkTimeCardTimeIsEmpty(): Boolean {
        var returnedBoolean = true
        adapterItems.forEach { item ->
            val hour = item.itemTimeCardWithButtonsHoursEditText.text.toString().toIntOrNull() ?: 0
            val min = item.itemTimeCardWithButtonsMinutesEditText.text.toString().toIntOrNull() ?: 0
            val sec = item.itemTimeCardWithButtonsSecondsEditText.text.toString().toIntOrNull() ?: 0
            if (mapTimeToMillis(hour, min, sec) == 0L){
                setBoxStrokeColor(
                    item.itemTimeCardWithButtonsHoursTextInputLayout,
                    item.itemTimeCardWithButtonsMinutesTextInputLayout,
                    item.itemTimeCardWithButtonsSecondsTextInputLayout,
                    color = R.color.text_input_layout_stroke_error_color
                )
                returnedBoolean = false
            }
        }
        return returnedBoolean
    }

    private fun setBoxStrokeColor(vararg inputLayout: TextInputLayout, color: Int) {
        inputLayout.forEach {
            it.setBoxStrokeColorStateList(
                AppCompatResources.getColorStateList(requireContext(), color)
            )
        }
    }

    private fun initAdapter() {
        binding.presetFragmentRecycler.adapter = adapter
        binding.presetFragmentRecycler.recycledViewPool.setMaxRecycledViews(0, 0)
    }
}