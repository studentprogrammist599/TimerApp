package com.kostry.yourtimer.ui.home

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.kostry.yourtimer.TimerWorker
import com.kostry.yourtimer.databinding.FragmentHomeBinding
import com.kostry.yourtimer.ui.base.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override fun getViewBinding(container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val workManager = WorkManager.getInstance(requireContext())
        workManager.enqueueUniqueWork(
            TimerWorker.TIMER_WORKER_NAME,
            ExistingWorkPolicy.REPLACE,
            TimerWorker.makeRequest(0, 10)
        )
    }
}