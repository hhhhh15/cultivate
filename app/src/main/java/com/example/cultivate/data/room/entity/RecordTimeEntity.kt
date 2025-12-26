package com.example.cultivate.data.room.entity

import androidx.room.Entity
import java.time.LocalDateTime

@Entity(tableName = "Record_Time")
data class RecordTime (
    val  Id: Int=0,
    val  taskId:String,
    val  userId:Int,
    val  setTime: LocalDateTime,
    val  remainTime: LocalDateTime
)






