package com.example.cultivate.data.retrofit.api

import com.example.cultivate.data.retrofit.response.ApiResponse
import com.example.cultivate.data.retrofit.model.SubTaskDto
import retrofit2.http.GET
import retrofit2.http.Query

interface subtaskInterface {

    @GET("showtasks")
    suspend fun getFourSubTask(): ApiResponse<Map<String, List<SubTaskDto>>>
    @GET("obtainsubtask")
    suspend fun getSubtaskByTaskID(@Query("taskId")taskId:Long): ApiResponse<List<SubTaskDto>>?


}