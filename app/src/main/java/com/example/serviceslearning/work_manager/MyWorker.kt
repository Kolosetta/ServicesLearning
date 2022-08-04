package com.example.serviceslearning.work_manager

import android.content.Context
import android.util.Log
import androidx.work.*

class MyWorker(context: Context, private val workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    override fun doWork(): Result {
        Log.i("Worker_Tag", "DoWork")
        val page = workerParameters.inputData.getInt(PAGE, 0)
        for (i in 0 until 5) {
            Thread.sleep(1000)
            Log.i("Worker_Tag", "Timer $i $page")
        }
        return Result.success()
    }

    companion object {
        private const val PAGE = "page"
        const val WORK_NAME = "random_name"

        fun makeRequest(page: Int): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<MyWorker>()
                .setInputData(workDataOf(PAGE to page)) //В workDataOf передается объект Pair
                .setConstraints(makeConstrains())
                .build()
        }

        private fun makeConstrains(): Constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .build()
    }
}