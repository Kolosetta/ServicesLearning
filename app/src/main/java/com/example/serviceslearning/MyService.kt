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
        Log.i("Service_Log", "OnStartCommand")

        coroutineScope.launch {
            for(i in 0 until 100){
                delay(1000)
                Log.i("Service_Log", "Secs: $i")
            }
        }

        return super.onStartCommand(intent, flags, startId)
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
        fun newIntent(context: Context): Intent{
            return Intent(context, MyService::class.java)
        }
    }
}