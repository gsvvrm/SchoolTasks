package com.gsa.schooltasks.database

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface JobDAO {

    //функция добавлени задания
    @Insert
    fun addJob (job: Job)

    // фукция запроса всех заданий
    @Query("SELECT * FROM job ORDER BY dateJob DESC")
    fun getJobs(): LiveData< List<Job> >

    // функция запроса конкретного задания
    @Query("SELECT * FROM job WHERE idJob=(:id)")
    fun getJob (id:UUID): LiveData< Job? >

    //обновление задания
    @Update
    fun updateJob (job: Job)

    // запрос невыполненных заданий по конкретному предмету
    @Query("SELECT * FROM job WHERE nameSubjectJob=(:nameSubjectJob) AND isSolvedJob = 0")
    fun getNoSolvedJob (nameSubjectJob: String):  LiveData<List<Job>>

    //задания на понедельник
    @Query("SELECT * FROM Job INNER JOIN Subject on Job.nameSubjectJob=Subject.nameSubject WHERE Job.isSolvedJob=0 AND Subject.onMondaySubj=1")
    fun getJobForMonday (): List<Job>

    //задания на вторник
    @Query("SELECT * FROM Job INNER JOIN Subject on Job.nameSubjectJob=Subject.nameSubject WHERE Job.isSolvedJob=0 AND Subject.onTuesdaySubj=1")
    fun getJobForTuesday (): List<Job>

    //задания на среду
    @Query("SELECT * FROM Job INNER JOIN Subject on Job.nameSubjectJob=Subject.nameSubject WHERE Job.isSolvedJob=0 AND Subject.onWednesdaySubj=1")
    fun getJobForWednesday (): List<Job>

    // задания на четверг
    @Query("SELECT * FROM Job INNER JOIN Subject on Job.nameSubjectJob=Subject.nameSubject WHERE Job.isSolvedJob=0 AND Subject.onThursdaySubj=1")
    fun getJobForThursday (): List<Job>

    // задания на пятницу
    @Query("SELECT * FROM Job INNER JOIN Subject on Job.nameSubjectJob=Subject.nameSubject WHERE Job.isSolvedJob=0 AND Subject.onFridaySubj=1")
    fun getJobForFriday (): List<Job>

    // задания на субботу
    @Query("SELECT * FROM Job INNER JOIN Subject on Job.nameSubjectJob=Subject.nameSubject WHERE Job.isSolvedJob=0 AND Subject.onSaturdaySubj=1")
    fun getJobForSaturday (): List<Job>

    // задания на воскресенье
    @Query("SELECT * FROM Job INNER JOIN Subject on Job.nameSubjectJob=Subject.nameSubject WHERE Job.isSolvedJob=0 AND Subject.onSundaySubj=1")
    fun getJobForSunday (): List<Job>

 ///////////////////////////////////////////////////////////////////////////////////

    //работа с предметами
    // функция добавлнения предмета
    @Insert
    fun addSubject (subject: Subject)

    //функция удаления предмета
    @Delete
    fun deleteSubject (subject: Subject)

    // функция получения списка предметов
    @Query("SELECT * FROM subject")
    fun getSubjects (): LiveData< List<Subject>>

    // функция получения списка наименований предметов
    @Query("SELECT nameSubject FROM subject")
    fun getNameSubjects (): LiveData< List<String?>>

    // функция получения конкретного предмета
    @Query("SELECT * FROM Subject WHERE nameSubject=(:nameSubject)")
    fun getSubject (nameSubject: String): LiveData <Subject?>

    // проверка существования предмета
    @Query("SELECT EXISTS(SELECT * FROM subject WHERE nameSubject =(:nameSubject))")
    fun isNameSubjRowIsExist(nameSubject: String) : LiveData <Boolean?>

    // запрос колличества предметов
    @Query("SELECT COUNT (*) FROM subject ")
    fun getCountSubjects (): LiveData<Int>

    // функция изменеия(редактирования) предмета
    @Update
    fun updateSubject (subject: Subject)

    // получение списка предметов для понедельника
    @Query("SELECT * FROM subject WHERE onMondaySubj")
    fun getSubjectsForMonday (): LiveData< List<Subject>>

    // получение списка предметов для вторника
    @Query("SELECT * FROM subject WHERE onTuesdaySubj")
    fun getSubjectsForTuesday (): LiveData< List<Subject>>

    // получение списка предметов для среды
    @Query("SELECT * FROM subject WHERE onWednesdaySubj")
    fun getSubjectsForWednesday (): LiveData< List<Subject>>

    // получение списка предметов для четверга
    @Query("SELECT * FROM subject WHERE onThursdaySubj")
    fun getSubjectsForThursday (): LiveData< List<Subject>>

    // получение списка предметов для пятницы
    @Query("SELECT * FROM subject WHERE onFridaySubj")
    fun getSubjectsForFriday (): LiveData< List<Subject>>

    // получение списка предметов для субботы
    @Query("SELECT * FROM subject WHERE onSaturdaySubj")
    fun getSubjectsForSaturday (): LiveData< List<Subject>>

    // получение списка предметов для воскресенье
    @Query("SELECT * FROM subject WHERE onSundaySubj")
    fun getSubjectsForSunday (): LiveData< List<Subject>>

}