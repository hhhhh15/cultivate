package com.example.cultivate.data.retrofit.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class TaskDto (
    @SerializedName("totalGoal")
    val subtasksCount: Int,

    @SerializedName("deadline")
    val deadlineDate: LocalDate,  // ❗️必须传

    val taskName: String,
    val startDate: LocalDate,
    val completed: Boolean,
    val difficulty: String,
    val divisionType: String,
    val subject: String,

    @SerializedName("tasks")
    val subtask:List<String>,

    val subtasksCompletedCount: Int?=0,
)

