package com.example.cultivate.data.retrofit

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

class ErrorInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            return chain.proceed(chain.request())
        } catch (e: IOException) {
            // 1️⃣ 判断是哪种异常
            val transformed = when (e) {
                is SocketTimeoutException -> NetworkException.Timeout("⏰请求超时，可能网络不稳定")
                is UnknownHostException -> NetworkException.NoNetwork("无法连接网络，请检查网络")
                is ConnectException -> NetworkException.ServerUnavailable("❌服务器无法连接")
                is SSLException -> NetworkException.SSL("证书错误或 HTTPS 问题")
                else -> NetworkException.Other("未知网络错误：${e.message}")
            }

            // 2️⃣ 在这里 throw 的不是原始异常，而是“自定义异常”
            throw transformed
        }
    }
}