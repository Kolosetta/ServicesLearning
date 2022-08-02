package com.example.serviceslearning

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import kotlinx.coroutines.*

class MyJobService : JobService() {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        Log.i("Service_Log", "OnCreate")
        super.onCreate()
    }


    override fun onDestroy() {
        Log.i("Service_Log", "OnDestroy")
        coroutineScope.cancel()
        super.onDestroy()
    }

    //Возвращаемый тип, обозначает, завершил ли сервис свою работу.
    //true, означает, что мы сами завершим работу сервиса
    override fun onStartJob(p0: JobParameters?): Boolean {
        Log.i("Service_Log", "OnStartCommand")
        coroutineScope.launch {
            for(i in 0 until 100){
                delay(1000)
                Log.i("Service_Log", "Secs: $i")
            }
            jobFinished(p0, false)
        }
        return true
    }

    //Вызывается только тогда, когда сервис перешел какое-либо заданное огранчиение работы
    //Не вызывается после естественного окончания работы сервиса
    //Возвращаемый тип, должен ли сервис быть запланирован заново на выполнение
    override fun onStopJob(p0: JobParameters?): Boolean {
        Log.i("Service_tag", "onStopJob")
        return false
    }
}