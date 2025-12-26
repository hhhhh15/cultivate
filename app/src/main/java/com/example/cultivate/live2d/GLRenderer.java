package com.example.cultivate.live2d;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

//这个相当于windows的窗口，用来创建窗口，设置窗口的大小的，还有while（）循环渲染，帧变化
public class GLRenderer implements GLSurfaceView.Renderer {

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            JniBridgeJava.nativeOnSurfaceCreated();
            }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
            JniBridgeJava.nativeOnSurfaceChanged(width, height);
            }

    @Override
    public void onDrawFrame(GL10 gl) {
            JniBridgeJava.nativeOnDrawFrame();
            }
}
