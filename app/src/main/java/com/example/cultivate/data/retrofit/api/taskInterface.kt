package com.example.cultivate.data.retrofit.api

import com.example.cultivate.data.retrofit.response.TaskResponse
import com.example.cultivate.data.retrofit.model.TaskDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface taskInterface {
    @POST("subtasks/manual")
    suspend fun postManualTasks(@Body a:TaskDto):Response<TaskResponse>

    @POST("subtasks/auto")
    suspend fun postAutoTasks(@Body a:TaskDto):Response<TaskResponse>


 }