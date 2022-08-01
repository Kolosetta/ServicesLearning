package com.example.serviceslearning

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.example.serviceslearning.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.serviceBtn.setOnClickListener{
            startService(MyService.newIntent(this, 10))
        }

        binding.foregroundBtn.setOnClickListener{
            showNotification()
        }
    }

    private fun showNotification(){

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

        //Создаем notification через компат, потмоу что в нем уже есть првоерка версии андроид
        //А на версии ниже 26 нельзя передавать( да и нет смысла) CHANNEL_ID
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("My Title")
            .setContentText("Text")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .build()
        
        notificationManager.notify(1, notification)
    }

    companion object{
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "channel_name"
    }
}