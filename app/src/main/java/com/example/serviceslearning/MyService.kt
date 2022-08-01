package com.example.serviceslearning


import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*

class MyService : Service() {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        Log.i("Service_Log", "OnCreate")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val startFrom = intent?.getIntExtra(EXTRA_START, 0) ?: 0

        Log.i("Service_Log", "OnStartCommand")

        coroutineScope.launch {
            for(i in startFrom until startFrom + 100){
                delay(1000)
                Log.i("Service_Log", "Secs: $i")
            }
        }
        //В случае уничтожение сервиса - перезапустит его, снова передав ему изначальный интент
        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        Log.i("Service_Log", "OnDestroy")
        coroutineScope.cancel()
        super.onDestroy()
    }


    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    companion object{

        private const val EXTRA_START = "start"

        fun newIntent(context: Context, startFrom: Int): Intent{
            return Intent(context, MyService::class.java).apply {
                putExtra(EXTRA_START, startFrom)
            }
        }
    }
}