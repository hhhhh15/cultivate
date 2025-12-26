package com.example.cultivate.data.room.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate


@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val userId: Int?=null,
    val taskName: String,
    val taskSubject: String,

    val startDate: LocalDate,
    val deadlineDate: LocalDate,
    val completed:Boolean,
    val difficulty: String,
    val subtaskCompleted:Int?=0,
    val subtasksCount: Int,

    //存一下后端的自增id,方便离线缓存后同步数据到后端
    val afterEndId:Long,
)