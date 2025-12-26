package com.example.cultivate.data.room.entity

import android.net.Uri
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName="subtask", foreignKeys = [ForeignKey(
    entity = Task::class,
    parentColumns = ["id"],
    childColumns = ["taskId"],
    onDelete = ForeignKey.CASCADE
)]
)
data class Subtask(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val subtaskUid:String,//这个还是有必要的，因为要上传到后端更新
    val subtaskName:String,
    val subject:String,
    val difficult:String,
    val completed:Boolean,
    val expectCompletedTime:LocalDate,

    val taskName:String,
    val userId:Int?=0,//为了方便之后本地缓存没了，登录后通过userid获取数据
    val afterEndTaskId:Long,

    //本地外键，主任务的值，    //android端需要
    val taskId:Int,
    val setTotalTime: LocalTime?=null,
    val remainTime: LocalTime?=null,
    var imageUri: Uri = Uri.EMPTY,

)
