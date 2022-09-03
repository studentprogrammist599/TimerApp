package com.kostry.yourtimer.ui.home

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.kostry.yourtimer.databinding.FragmentHomeBinding
import com.kostry.yourtimer.ui.base.BaseFragment
import com.kostry.yourtimer.util.ViewModelFactory
import javax.inject.Inject

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        (requireActivity().application as HomeSubcomponentProvider)
            .initHomeSubcomponent()
            .inject(this)
        super.onAttach(context)
    }

    override fun getViewBinding(container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}