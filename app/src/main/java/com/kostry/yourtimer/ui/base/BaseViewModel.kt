package com.kostry.yourtimer.ui.base

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kostry.yourtimer.util.AppState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.plus

private const val COROUTINE_EXCEPTION_HANDLER = "COROUTINE_EXCEPTION_HANDLER"

abstract class BaseViewModel : ViewModel() {

    protected val mutableStateFlow = MutableStateFlow<Long>(0)
    val stateFlow = mutableStateFlow.asStateFlow()

    @SuppressLint("LongLogTag")
    private val coroutineContext =
        SupervisorJob() + Dispatchers.Main.immediate + CoroutineExceptionHandler { _, throwable ->
            Log.e(COROUTINE_EXCEPTION_HANDLER, throwable.message.toString())
        }
    protected val baseViewModelScope = viewModelScope.plus(coroutineContext)
}