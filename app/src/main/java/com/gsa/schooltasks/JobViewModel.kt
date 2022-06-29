package com.gsa.schooltasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.gsa.schooltasks.database.Job
import com.gsa.schooltasks.database.Subject
import java.util.*

class JobViewModel: ViewModel() {


    private val jobRepository = JobRepository.get()
    private val subjectNameLiveData = MutableLiveData<String>()
    private val jobIdLiveData = MutableLiveData<UUID>()

    var jobLiveData: LiveData<Job?> =
        Transformations.switchMap(jobIdLiveData) { jobId ->
            jobRepository.getJob(jobId)
        }

    //работа с заданиями

    //добавление задания
    fun addJob (job: Job) = jobRepository.addJob(job)

    //получение одного задания
    fun loadJob (jobId: UUID) {
        jobIdLiveData.value = jobId
    }

    //обновление задания
    fun updateJob (job: Job){
        jobRepository.updateJob(job)
    }


    // полученме списка заданий
    val jobListLiveData = jobRepository.getJobs()

    // полученме списка не выполненных заданий по конкретному предмету
    fun getNoSolvedJobsLiveData (nameSubjectJob: String): LiveData< List<Job> > = jobRepository.getNoSolvedJobs(nameSubjectJob)

    // задания на каждый день не выполненные
//    val jobListMondayLiveData = jobRepository.getJobsForMonday()
//    val jobListTuesdayLiveData = jobRepository.getJobsForTuesday()
//    val jobListWednesdayLiveData = jobRepository.getJobsForWednesday()
//    val jobListThursdayLiveData = jobRepository.getJobsForThursday()
//    val jobListFridayLiveData = jobRepository.getJobsForFriday()
//    val jobListSaturdayLiveData = jobRepository.getJobsForSaturday()
//    val jobListSundayLiveData = jobRepository.getJobsForSunday()



    //работа с предметам
    //добавление предмета
    fun addSubject (subject: Subject) = jobRepository.addSubject(subject)

    //удаление предмета
    fun deleteSubject (subject: Subject) = jobRepository.deleteSubject(subject)

    //получение списка предметов
    val subjectListLiveData = jobRepository.getSubjects()

    //получение списка наименований предметов
    val subjectNameListLiveData = jobRepository.getNameSubjects()

    //получение одного предмета
    var subjectLiveData: LiveData<Subject?> =
        Transformations.switchMap(subjectNameLiveData) {
            subjectName -> jobRepository.getSubject(subjectName)
        }

    fun loadSubject ( nameSubject: String) {
        subjectNameLiveData.value = nameSubject
    }

    //изменение (редактирование) передмета
    fun updateSubject (subject: Subject) = jobRepository.updateSubject(subject)

    // проверка существования предмета
    fun isNameSubjRowIsExist(nameSubject: String): LiveData<Boolean?> {
        return jobRepository.isNameSubjRowIsExist(nameSubject)
    }

    //получение колличества предметов
    fun getCountSubjects (): LiveData<Int> = jobRepository.getCountSubjects()

    // получение спика предметов для дней недели
    fun getSubjectsForMonday (): LiveData<List<Subject>> = jobRepository.getSubjectsForMonday()
    fun getSubjectsForTuesday (): LiveData<List<Subject>> = jobRepository.getSubjectsForTuesday()
    fun getSubjectsForWednesday (): LiveData<List<Subject>> = jobRepository.getSubjectsForWednesday()
    fun getSubjectsForThursday (): LiveData<List<Subject>> = jobRepository.getSubjectsForThursday()
    fun getSubjectsForFriday (): LiveData<List<Subject>> = jobRepository.getSubjectsForFriday()
    fun getSubjectsForSaturday (): LiveData<List<Subject>> = jobRepository.getSubjectsForSaturday()
    fun getSubjectsForSunday (): LiveData<List<Subject>> = jobRepository.getSubjectsForSunday()
}