//
// Created by 86158 on 2025/7/15.
//
#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_cutivatingapp1_1java_MainActivity_stringFromJNI__(JNIEnv* env, jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
