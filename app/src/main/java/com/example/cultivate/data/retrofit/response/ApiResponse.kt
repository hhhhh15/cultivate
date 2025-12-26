package com.example.cultivate.data.retrofit.response

data class ApiResponse<T>(
    val code: Int,
    val message: String,
    val data: T?
)
