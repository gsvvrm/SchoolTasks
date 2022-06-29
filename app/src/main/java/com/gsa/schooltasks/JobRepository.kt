package com.gsa.schooltasks

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.gsa.schooltasks.database.Job
import com.gsa.schooltasks.database.JobDatabase
import com.gsa.schooltasks.database.Subject
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors

private const val TAG = "JobRepository"

private const val DATABASE_NAME = "job-database"

// репозиторий для подключения к базе даных
class JobRepository private constructor(context: Context){

    private val database: JobDatabase = Room.databaseBuilder(
        context.applicationContext,
        JobDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val jobDAO = database.jobDao()

    private val executor = Executors.newSingleThreadExecutor()

    //добавление задания
    fun addJob (job: Job) {
        executor.execute {
            jobDAO.addJob(job)
        }
    }

    //получение списка заданий
    fun getJobs(): LiveData< List<Job> > = jobDAO.getJobs()

    // получение конкретного задания
    fun getJob(id:UUID): LiveData< Job? > = jobDAO.getJob(id)

    //обновление задания
    fun updateJob (job: Job){
        executor.execute {
            jobDAO.updateJob(job)
        }
    }

    //получение списка не выполненных заданий по конкретному предмету
    fun getNoSolvedJobs(nameSubjectJob: String): LiveData< List<Job> > = jobDAO.getNoSolvedJob(nameSubjectJob)

    //задания на каждый день не выполненные
    fun getJobsForMonday(): List<Job> = jobDAO.getJobForMonday()
    fun getJobsForTuesday(): List<Job> = jobDAO.getJobForTuesday()
    fun getJobsForWednesday(): List<Job> = jobDAO.getJobForWednesday()
    fun getJobsForThursday(): List<Job> = jobDAO.getJobForThursday()
    fun getJobsForFriday(): List<Job> = jobDAO.getJobForFriday()
    fun getJobsForSaturday(): List<Job> = jobDAO.getJobForSaturday()
    fun getJobsForSunday():  List<Job> = jobDAO.getJobForSunday()



    //работа с предметами
    // добавление предмета
    fun addSubject (subject: Subject) {
        executor.execute {
            jobDAO.addSubject(subject)
        }
    }

    //удаление предмета
    fun deleteSubject (subject: Subject) {
        executor.execute {
            jobDAO.deleteSubject(subject)
        }
    }

    // получение списка предметов
    fun getSubjects (): LiveData<List<Subject>> = jobDAO.getSubjects()

    //получение списка наименований предметов
    fun getNameSubjects (): LiveData<List<String?>> = jobDAO.getNameSubjects()

    // получение одного предмета
    fun getSubject (nameSubject: String): LiveData<Subject?> = jobDAO.getSubject(nameSubject)

    //изменение (редактирование) пердмета
    fun updateSubject (subject: Subject) {
        executor.execute {
            jobDAO.updateSubject(subject)
        }
    }

    //проверка существования предмета
    fun isNameSubjRowIsExist(nameSubject: String) : LiveData<Boolean?> {
        return  jobDAO.isNameSubjRowIsExist(nameSubject)

    }

    //колличество предметов
    fun getCountSubjects (): LiveData<Int> = jobDAO.getCountSubjects()

    // получение списка предметов для дней недели
    fun getSubjectsForMonday (): LiveData<List<Subject>> = jobDAO.getSubjectsForMonday()
    fun getSubjectsForTuesday (): LiveData<List<Subject>> = jobDAO.getSubjectsForTuesday()
    fun getSubjectsForWednesday (): LiveData<List<Subject>> = jobDAO.getSubjectsForWednesday()
    fun getSubjectsForThursday (): LiveData<List<Subject>> = jobDAO.getSubjectsForThursday()
    fun getSubjectsForFriday (): LiveData<List<Subject>> = jobDAO.getSubjectsForFriday()
    fun getSubjectsForSaturday (): LiveData<List<Subject>> = jobDAO.getSubjectsForSaturday()
    fun getSubjectsForSunday (): LiveData<List<Subject>> = jobDAO.getSubjectsForSunday()




    companion object{
        private var INSTANCE: JobRepository? = null
                fun initialize (context: Context) {
                    if (INSTANCE == null){
                        INSTANCE= JobRepository(context)
                    }
                }

        fun get():JobRepository{
            return INSTANCE?:
            throw IllegalStateException("JobRepository must be initialized")
        }
    }

}