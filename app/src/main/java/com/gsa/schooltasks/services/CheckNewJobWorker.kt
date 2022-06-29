package com.gsa.schooltasks.services

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.gsa.schooltasks.DayOfWeekActivity
import com.gsa.schooltasks.JobRepository
import com.gsa.schooltasks.R
import com.gsa.schooltasks.application.NOTIFICATION_CHANNEL_ID
import com.gsa.schooltasks.database.Job
import java.util.*

private const val TAG = "CheckNewJobWorker"

class CheckNewJobWorker (val context: Context, workerParams: WorkerParameters): Worker (context, workerParams) {

    private val jobRepository = JobRepository.get()
    private val calendar: Calendar = Calendar.getInstance()
    var dayIndex: Int = 0

    override fun doWork(): Result {

        val countJob = getCountJob().size
        Log.i(TAG, "$countJob новых заданий")

        if (countJob == 0) {
            Log.i(TAG, "Нет новых заданий")
        } else {
            val intent = Intent(context, DayOfWeekActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context,0,intent,0)

            val resources = context.resources
            val notification = NotificationCompat
                .Builder(context, NOTIFICATION_CHANNEL_ID)
                .setTicker(resources.getString(R.string.notifi_new_jobs_title))
                .setSmallIcon(R.drawable.ic_action_name)
                .setContentTitle(resources.getString(R.string.notifi_new_jobs_title))
                .setContentText("Заданий к выполнению: $countJob")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(0,notification)

        }

        return Result.success()
    }

    private fun getCountJob(): List<Job> {

        dayIndex = calendar.get(Calendar.DAY_OF_WEEK)
        return when (dayIndex){
            1 ->jobRepository.getJobsForMonday()
            2 ->jobRepository.getJobsForTuesday()
            3 ->jobRepository.getJobsForWednesday()
            4 ->jobRepository.getJobsForThursday()
            5 ->jobRepository.getJobsForFriday()
            6 ->jobRepository.getJobsForSaturday()
            7 ->jobRepository.getJobsForSunday()
            else ->jobRepository.getJobsForMonday()
        }

    }

}