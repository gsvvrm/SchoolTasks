package com.gsa.schooltasks.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Job::class,Subject::class],version = 1)
@TypeConverters(JobTypeConverters::class)
abstract class JobDatabase: RoomDatabase() {

    abstract fun jobDao(): JobDAO

}