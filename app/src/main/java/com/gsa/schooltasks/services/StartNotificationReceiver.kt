package com.gsa.schooltasks.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.os.Build

private const val TAG = "StartNotificationReceiver"

class StartNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        Log.i(TAG, "Запуск рессивера")

        //запуск службы проверки новых заданий
        val intentService = Intent(context,CheckNewJobService::class.java)
        Log.i(TAG, "старт запуска службы")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService)
        } else {
            context.startService(intentService)
        }
        Log.i(TAG, "служба запущена")

    }
}