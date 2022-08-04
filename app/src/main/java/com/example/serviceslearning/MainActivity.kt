package com.example.serviceslearning

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.startForegroundService
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

        binding.notificationBtn.setOnClickListener{
            showNotification()
        }

        binding.foregroundBtn.setOnClickListener{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(MyForegroundService.newIntent(this))
            }
            else
            {
                startService(MyService.newIntent(this, 0))
            }
        }

        binding.intentServiceBtn.setOnClickListener{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(MyIntentService.newIntent(this))
            }
        }

        binding.jobSchedulerBtn.setOnClickListener{
            val componentName = ComponentName(this, MyJobService::class.java)
            //Установка ограничений работы сервиса
            val jobInfo = JobInfo.Builder(MyJobService.JOB_ID, componentName)
                .setRequiresCharging(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .build()
            //Запуск сервиса
            val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
            jobScheduler.schedule(jobInfo)
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