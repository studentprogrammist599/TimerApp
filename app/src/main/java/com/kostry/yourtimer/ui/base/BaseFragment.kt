package com.kostry.yourtimer.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.cancelChildren

abstract class BaseFragment<VB : ViewBinding>: Fragment() {

    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding ?: throw RuntimeException("ViewBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(container)
        return binding.root
    }

    abstract fun getViewBinding(container: ViewGroup?): VB

    override fun onStop() {
        super.onStop()
        lifecycleScope.coroutineContext.cancelChildren()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}