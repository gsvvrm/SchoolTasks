package com.gsa.schooltasks.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Job (@PrimaryKey val idJob: UUID = UUID.randomUUID(),
                var nameSubjectJob: String = "",
                var contentJob: String = "",
                var dateJob: Date = Date(),
                var isSolvedJob: Boolean = false)