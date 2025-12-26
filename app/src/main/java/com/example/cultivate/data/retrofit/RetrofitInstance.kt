package com.example.cultivate.data.retrofit

import com.example.cultivate.data.retrofit.api.subtaskInterface
import com.example.cultivate.data.retrofit.api.taskInterface
import com.example.cultivate.data.retrofit.api.testRetrofit
import com.google.gson.*
import com.google.gson.GsonBuilder
import com.google.gson.JsonPrimitive
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.time.LocalDate
import java.util.concurrent.TimeUnit


object RetrofitInstance {
    private val logging = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY) // ✅ 看到 Header + JSON Body + Response
    }

    private val gson = GsonBuilder()//JsonSerializer序列化，将LocalDate转换成JSON对象
        .registerTypeAdapter(LocalDate::class.java, JsonSerializer<LocalDate> { src, _, _ ->
            JsonPrimitive(src.toString())
        })
        //JsonDeserializer反序列化，将JSON对象转换成LocalDate对象
        .registerTypeAdapter(LocalDate::class.java, JsonDeserializer<LocalDate> { json, _, _ ->
            LocalDate.parse(json.asString)
        })
        .create()


    // OkHttp 引擎（真正执行 HTTP 请求）
    private val client = OkHttpClient.Builder()
        .addInterceptor(ErrorInterceptor())//添加全局拦截器
        .addInterceptor(logging)  // ✅ 装入引擎
        .connectTimeout(3, TimeUnit.SECONDS)//3s没有连接上，设置连接超时抛出异常
        .readTimeout(5, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    val retrofit= Retrofit.Builder()
        .baseUrl("http://192.168.1.27:8081/")
        .client(client)  // ✅ 绑定 OkHttpClient，否则拦截器不会生效
//        .addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson)) // ✅ 使用自定义 gson
        .build()

    //在这里创建retrofit的接口的实现类
    val testApi=retrofit.create(testRetrofit::class.java)
    val taskApi= retrofit.create(taskInterface::class.java)
    val subtaskApi= retrofit.create(subtaskInterface::class.java)
}