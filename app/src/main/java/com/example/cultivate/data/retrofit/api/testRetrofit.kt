package com.example.cultivate.data.retrofit.api

import com.example.cultivate.data.retrofit.model.test
import retrofit2.http.Body
import retrofit2.http.POST

interface testRetrofit {
    @POST("testRetrofit")
    suspend fun testPost(@Body hh:test):String
}