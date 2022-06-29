package com.gsa.schooltasks.services

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

private const val TAG = "CheckNewJobService"

private const val WORK_CHECK_NEW_JOB = "CHECK_NEW_JOB"

class CheckNewJobService : Service() {

    override fun startService(service: Intent?): ComponentName? {
        return super.startService(service)
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {


    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.i(TAG, "Запуск службы проверки заданий")

        val checkWorker = PeriodicWorkRequest.Builder(CheckNewJobWorker::class.java,1,TimeUnit.HOURS).build()
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(WORK_CHECK_NEW_JOB,ExistingPeriodicWorkPolicy.REPLACE,checkWorker)


        return START_STICKY

    }



}