package com.gsa.schooltasks.application

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import com.gsa.schooltasks.JobRepository
import com.gsa.schooltasks.R
import com.gsa.schooltasks.services.CheckNewJobService
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig


private const val TAG = "SchoolTasksApplication"

const val  NOTIFICATION_CHANNEL_ID = "school_tasks"

//класс приложения для инициализации репозитория и канала уведомления
class SchoolTasksApplication: Application() {

    private var manufacturer: String = ""
    private var model: String = ""
    private var versionSDK: Int = 0
    private lateinit var checkNewJobService: CheckNewJobService

    override fun onCreate() {
        super.onCreate()

        //получение информации об устройстве
        manufacturer = Build.MANUFACTURER
        model = Build.MODEL
        versionSDK = Build.VERSION.SDK_INT
        Log.i(TAG, " \nManufacturer: $manufacturer \nModel: $model \nSDK: $versionSDK")


        //создание репозитория БД
        JobRepository.initialize(this)

        //создание канала уведомдения
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notification_channel_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID,name,importance)
            val notificationManager: NotificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        activateAppMetrica()

    }

    private fun activateAppMetrica() {
        val appMetricaConfig: YandexMetricaConfig =
            YandexMetricaConfig.newConfigBuilder("6fb2f160-7b3b-45eb-b290-198c0958c05b")
                .withLocationTracking(true)
                .withStatisticsSending(true)
                .build()
        YandexMetrica.activate(applicationContext, appMetricaConfig)
    }

}