package com.example.serviceslearning

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*

class MyForegroundService : Service() {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        Log.i("Service_Log", "OnCreate Foreground")
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.i("Service_Log", "OnStartCommand Foreground")

        coroutineScope.launch {
            for(i in 0 until 10){
                delay(1000)
                Log.i("Service_Log", "Secs: $i")
            }
            stopSelf()
        }
        return START_STICKY
    }

    override fun onDestroy() {
        Log.i("Service_Log", "OnDestroy Foreground")
        coroutineScope.cancel()
        super.onDestroy()
    }

    private fun createNotificationChannel(){
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        //Проверка. Если текущая версия больше чем 8, то создаем channel.
        //Иначе, она не нужен для показа уведомлений
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun createNotification(): Notification {
        //Создаем notification через компат, потмоу что в нем уже есть првоерка версии андроид
        //А на версии ниже 26 нельзя передавать(да и нет смысла) CHANNEL_ID
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("My Title")
            .setContentText("Text")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .build()
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    companion object{
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "channel_name"
        private const val NOTIFICATION_ID = 1

        fun newIntent(context: Context): Intent{
            return Intent(context, MyForegroundService::class.java)
        }
    }
}