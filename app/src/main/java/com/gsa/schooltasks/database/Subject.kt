package com.gsa.schooltasks.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Subject (@PrimaryKey (autoGenerate = true) var idSubject: Int = 0,
                    var nameSubject:String = "",
                    val onMondaySubj: Boolean = false,
                    val onTuesdaySubj: Boolean = false,
                    val onWednesdaySubj: Boolean = false,
                    val onThursdaySubj: Boolean = false,
                    val onFridaySubj: Boolean = false,
                    val onSaturdaySubj: Boolean = false,
                    val onSundaySubj: Boolean = false)