package com.example.cultivate.ui.activity

import android.app.Activity
import android.opengl.GLSurfaceView
import android.os.Build
import android.os.Bundle
import com.example.cultivate.live2d.GLRenderer
import com.example.cultivate.live2d.JniBridgeJava
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import java.io.IOException


class Live2DActivity : Activity() {

    private lateinit var glSurfaceView: GLSurfaceView
    private lateinit var glRenderer: GLRenderer

    // --- 触摸事件转发到 Native ---
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        glSurfaceView.queueEvent {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> JniBridgeJava.nativeOnTouchesBegan(x, y)
                MotionEvent.ACTION_UP -> JniBridgeJava.nativeOnTouchesEnded(x, y)
                MotionEvent.ACTION_MOVE -> JniBridgeJava.nativeOnTouchesMoved(x, y)
            }
        }
        return super.onTouchEvent(event)
    }

    // --- 生命周期 ---
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 让 Java 层的 JNIBridge 拿到 context
        JniBridgeJava.SetActivityInstance(this)
        JniBridgeJava.SetContext(this)
        // 打印 assets 里的文件
        logAssetsRecursively("Zombie")
        // 创建 GLSurfaceView
        glSurfaceView = GLSurfaceView(this)
        glSurfaceView.setEGLContextClientVersion(2)

        // 你的 GLRenderer（必须实现 GLSurfaceView.Renderer 接口）
        glRenderer = GLRenderer()
        glSurfaceView.setRenderer(glRenderer)
        glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY

        // 设置显示内容
        setContentView(glSurfaceView)

        // 全屏沉浸
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.let {
                it.hide(WindowInsets.Type.systemBars())
                it.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        }

    }
    private fun logAssetsRecursively(path: String = "") {
        try {
            val list = assets.list(path)
            if (list != null) {
                for (item in list) {
                    val fullPath = if (path.isEmpty()) item else "$path/$item"
                    println("Asset: $fullPath")
                    // 如果是目录，递归
                    if (assets.list(fullPath)?.isNotEmpty() == true) {
                        logAssetsRecursively(fullPath)
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onStart() {
        super.onStart()
        JniBridgeJava.nativeOnStart()
    }

    override fun onResume() {
        super.onResume()
        glSurfaceView.onResume()
    }

    override fun onPause() {
        glSurfaceView.onPause()
        JniBridgeJava.nativeOnPause()
        super.onPause()
    }

    override fun onStop() {
        JniBridgeJava.nativeOnStop()
        super.onStop()
    }

    override fun onDestroy() {
        JniBridgeJava.nativeOnDestroy()
        super.onDestroy()
    }
}