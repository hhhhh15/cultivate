package com.example.cultivate.data.retrofit

import java.io.IOException


sealed class NetworkException(msg: String) : IOException(msg) {
    class Timeout(msg: String): NetworkException(msg)
    class NoNetwork(msg: String): NetworkException(msg)
    class ServerUnavailable(msg: String): NetworkException(msg)
    class SSL(msg: String): NetworkException(msg)
    class Other(msg: String): NetworkException(msg)
}