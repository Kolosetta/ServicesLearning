package com.example.serviceslearning

import android.app.IntentService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat

//Устаревший сервис
class MyIntentService : IntentService(NAME) {

    override fun onCreate() {
        Log.i("Service_Log", "OnCreate")
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())

        //Перезапуск сервиса с переданным интентом, в случае убийства сервиса системой
        setIntentRedelivery(true)
        super.onCreate()
    }

    //Код внутри этого метода будет выполняться в другом потоке
    override fun onHandleIntent(p0: Intent?) {
        for (i in 0 until 10) {
            Thread.sleep(1000)
            Log.i("Service_Log", "Secs: $i")
        }
        //После окончания выполнения, сервис остановится автоматически
    }

    override fun onDestroy() {
        Log.i("Service_Log", "OnDestroy")
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

    companion object {
        private const val NAME = "my_intent_service"
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "channel_name"
        private const val NOTIFICATION_ID = 1

        fun newIntent(context: Context): Intent{
            return Intent(context, MyIntentService::class.java)
        }
    }

}