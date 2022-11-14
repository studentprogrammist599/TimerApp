package com.kostry.yourtimer.ui.preset

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.kostry.yourtimer.R
import com.kostry.yourtimer.databinding.FragmentPresetBinding
import com.kostry.yourtimer.databinding.ItemTimeCardWithButtonsBinding
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
            val presetNameIsEmpty = checkPresetNameTextIsEmpty()
            val cardsNamesIsEmpty = checkTimeCardNameIsEmpty()
            if (presetNameIsEmpty && cardsNamesIsEmpty) {
                Toast.makeText(context, "Preset saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Preset fields is empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initTextFieldsListeners() {
        binding.presetFragmentPresetNameEditText.addTextChangedListener {
            binding.presetFragmentTextInputLayout.error = null
        }
    }

    private fun checkPresetNameTextIsEmpty(): Boolean {
        if (binding.presetFragmentPresetNameEditText.text.toString().isEmpty()) {
            binding.presetFragmentTextInputLayout.error = getErrorTextName()
            return false
        }
        return true
    }

    private fun checkTimeCardNameIsEmpty(): Boolean {
        var returnedBoolean = true
        adapterItems.forEach { item ->
            if (item.itemTimeCardWithButtonsTextNameEditText.text.toString().isEmpty()) {
                item.itemTimeCardWithButtonsNameError.visibility = View.VISIBLE
                returnedBoolean = false
            }
            if (item.itemTimeCardWithButtonsRepsEditText.text.toString().isEmpty()) {
                item.itemTimeCardWithButtonsTimeError.visibility = View.VISIBLE
                returnedBoolean = false
            }
            if (item.itemTimeCardWithButtonsHoursEditText.text.toString().isEmpty()) {
                item.itemTimeCardWithButtonsTimeError.visibility = View.VISIBLE
                returnedBoolean = false
            }
            if (item.itemTimeCardWithButtonsMinutesEditText.text.toString().isEmpty()) {
                item.itemTimeCardWithButtonsTimeError.visibility = View.VISIBLE
                returnedBoolean = false
            }
            if (item.itemTimeCardWithButtonsSecondsEditText.text.toString().isEmpty()) {
                item.itemTimeCardWithButtonsTimeError.visibility = View.VISIBLE
                returnedBoolean = false
            }
        }
        return returnedBoolean
    }

    private fun getErrorTextName(): String {
        return requireContext().resources.getString(R.string.error_field_is_empty)
    }

    private fun initAdapter() {
        binding.presetFragmentRecycler.adapter = adapter
        binding.presetFragmentRecycler.recycledViewPool.setMaxRecycledViews(0, 0)
    }
}