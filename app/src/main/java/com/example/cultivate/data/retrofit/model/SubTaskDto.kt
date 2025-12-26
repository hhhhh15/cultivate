package com.example.cultivate.data.retrofit.model

import android.net.Uri
import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.LocalTime

data class SubTaskDto(
    val subTaskUid: String,
    val subTaskName: String,
    val completed: Boolean,

    @SerializedName("completedtime")
    val expectCompletedTime: LocalDate,

    val subject: String,
    val difficult: String,

    @SerializedName("taskname")
    val taskName: String,
    val taskId: Long,

    //android端需要,暂时需要
    val id:Int=0,
    val setTotalTime: LocalTime?,
    val remainTime: LocalTime?,
    var imageUri: Uri = Uri.EMPTY,
)

