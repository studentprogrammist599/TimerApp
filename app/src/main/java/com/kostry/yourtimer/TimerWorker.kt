package com.kostry.yourtimer

import android.content.Context
import android.util.Log
import androidx.work.*
import kotlinx.coroutines.delay

class TimerWorker(
    context: Context,
    private val workerParameters: WorkerParameters
): CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        log("on doWork")
        val startTime: Int = workerParameters.inputData.getInt(START_TIME, 0)
        val endTime: Int = workerParameters.inputData.getInt(END_TIME, startTime)
        for (i in startTime..endTime) {
            delay(1000)
            log("START time = $i")
        }
        return Result.success()
    }

    private fun log(message: String){
        Log.d(WORKER_TAG, "MyWorker: $message")
    }

    companion object {
        const val TIMER_WORKER_NAME = "TIMER_WORKER_NAME"
        const val WORKER_TAG = "WORKER_TAG"
        private const val START_TIME = "START_TIME"
        private const val END_TIME = "END_TIME"

        fun makeRequest(startTime: Int, endTime: Int): OneTimeWorkRequest{
            return OneTimeWorkRequestBuilder<TimerWorker>()
                .setInputData(workDataOf(START_TIME to startTime, END_TIME to endTime))
                .setConstraints(makeConstraints())
                .build()
        }

        private fun makeConstraints(): Constraints{
            return Constraints.Builder().build()
        }
    }
}