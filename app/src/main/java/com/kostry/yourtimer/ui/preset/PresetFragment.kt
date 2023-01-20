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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputLayout
import com.kostry.yourtimer.R
import com.kostry.yourtimer.databinding.FragmentPresetBinding
import com.kostry.yourtimer.datasource.models.TimeCardModel
import com.kostry.yourtimer.di.provider.PresetSubcomponentProvider
import com.kostry.yourtimer.ui.base.BaseFragment
import com.kostry.yourtimer.util.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class PresetFragment : BaseFragment<FragmentPresetBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: PresetViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PresetViewModel::class.java]
    }

    private val args by navArgs<PresetFragmentArgs>()

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
        if (args.preset != null && savedInstanceState == null) {
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
            viewModel.timeCards()
            val presetNameIsEmpty = checkPresetNameIsEmpty()
            val cardsNamesIsEmpty = adapter.checkTimeCardNameIsEmpty()
            val cardsTimesIsEmpty = adapter.checkTimeCardTimeIsEmpty()
            val cardsRepsIsEmpty = adapter.checkTimeCardRepsIsEmpty()
            if (presetNameIsEmpty && cardsNamesIsEmpty && cardsTimesIsEmpty && cardsRepsIsEmpty) {
                viewModel.savePreset(binding.presetFragmentPresetNameEditText.text.toString())
                Toast.makeText(
                    context,
                    requireContext().resources.getString(R.string.preset_saved),
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().popBackStack()
            } else {
                Toast.makeText(
                    context,
                    requireContext().resources.getString(R.string.preset_fields_is_empty),
                    Toast.LENGTH_SHORT
                ).show()
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