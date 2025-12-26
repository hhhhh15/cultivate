plugins {
    id("com.android.application")
    // Add the Google services Gradle plugin
    //id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")

}

android {
    namespace = "com.example.cultivate"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.cultivate"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        ndk {
            abiFilters.addAll(listOf( "arm64-v8a","x86","x86_64"))
        }

        externalNativeBuild {
            cmake {
                cppFlags.addAll(listOf("-std=c++17", "-frtti", "-fexceptions"))
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }



    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    //打开viewBinding
    buildFeatures {
        viewBinding = true
        dataBinding = true      // ✅ 启用 DataBinding（关键！）
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    externalNativeBuild {
        cmake {
            // ❗️注意这个 file() 不要改写为字符串
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }

}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("com.kizitonwose.calendar:view:2.6.0")

    // Media3 ExoPlayer 核心
    implementation("androidx.media3:media3-exoplayer:1.2.1")

    // UI（PlayerView）
    implementation("androidx.media3:media3-ui:1.2.1")

    // 网络（HTTP / HTTPS）
    implementation("androidx.media3:media3-datasource-okhttp:1.2.1")
    //网络协议
    implementation ("com.squareup.okhttp3:okhttp:4.9.1")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")//OkHttpClient的日志拦截器依赖

    // Kotlin 支持Jackson 核心库（可选，但推荐）
    implementation ("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")

    //kclass反射包
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.10")
    //Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("androidx.compose.ui:ui-desktop:1.7.0")
    kapt("com.github.bumptech.glide:compiler:4.16.0")
    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1") // 注意这是 kapt 而不是 implementation！


// 可选：如果你使用 Kotlin 协程支持
    implementation("androidx.room:room-ktx:2.6.1")

// 可选：如果你要在测试中用 Room
    testImplementation("androidx.room:room-testing:2.6.1")

// Lifecycle ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
// Lifecycle LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment:2.6.0")
    implementation("androidx.navigation:navigation-ui:2.6.0")
    implementation("androidx.core:core-ktx:1.12.0")


    // CameraX 核心功能
    implementation("androidx.camera:camera-core:1.3.0")
    implementation("androidx.camera:camera-camera2:1.3.0")

// CameraX 生命周期和视图支持
    implementation("androidx.camera:camera-lifecycle:1.3.0")
    implementation("androidx.camera:camera-view:1.3.0")

// 如需录像功能（可选）
    implementation("androidx.camera:camera-video:1.3.0")

//语音识别使用讯飞api
    // 讯飞SDK
//    implementation ("com.iflytek:libMsc:1.0.1234")
    // 网络请求
    implementation ("com.squareup.okhttp3:okhttp:4.9.3")
    // JSON解析
    implementation ("com.google.code.gson:gson:2.8.9")

    //测试连接
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //recycleview的使用
    implementation("androidx.recyclerview:recyclerview:1.3.0")
    implementation("androidx.recyclerview:recyclerview-selection:1.2.0-alpha01")




}